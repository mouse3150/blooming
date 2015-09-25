package cn.com.esrichina.adapter.vcloud.domain;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeoutException;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.AdapterRuntimeException;
import cn.com.esrichina.adapter.commons.ComputeResource;
import cn.com.esrichina.adapter.commons.GuestCustomizationOptions;
import cn.com.esrichina.adapter.commons.HardwareOptions;
import cn.com.esrichina.adapter.commons.LaunchVmOptions;
import cn.com.esrichina.adapter.commons.NetworkConfigOptions;
import cn.com.esrichina.adapter.commons.OsInfo;
import cn.com.esrichina.adapter.commons.Platform;
import cn.com.esrichina.adapter.commons.VirtualMachineConfiguration;
import cn.com.esrichina.adapter.commons.VirtualMachineRuntimeInfo;
import cn.com.esrichina.adapter.commons.VmStatus;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IDatastore;
import cn.com.esrichina.adapter.domain.IHost;
import cn.com.esrichina.adapter.domain.INetwork;
import cn.com.esrichina.adapter.domain.IVirtualMachine;

import com.vmware.vcloud.api.rest.schema.GuestCustomizationSectionType;
import com.vmware.vcloud.api.rest.schema.NetworkConnectionSectionType;
import com.vmware.vcloud.api.rest.schema.NetworkConnectionType;
import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.VM;
import com.vmware.vcloud.sdk.Vapp;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.Vdc;
import com.vmware.vcloud.sdk.VirtualCpu;
import com.vmware.vcloud.sdk.VirtualDisk;
import com.vmware.vcloud.sdk.VirtualMemory;
import com.vmware.vcloud.sdk.constants.IpAddressAllocationModeType;
import com.vmware.vcloud.sdk.constants.UndeployPowerActionType;
import com.vmware.vcloud.sdk.constants.VMStatus;

public class VCloudVirtualMachine implements IVirtualMachine {
	private static final int EACH_SIZE = 1024;
	private static final int REFRESH_NUM = 10;
	private static final int REFRESH_INTERVAL = 6000;
	

	private VcloudClient vcloudClient;
	private VM vm;
	private Vapp ownerVapp;

	public VCloudVirtualMachine(VcloudClient vcloudClient, VM vm) {
		this.vcloudClient = vcloudClient;
		this.vm = vm;
//		try {
//			ownerVapp = Vapp.getVappByReference(vcloudClient, vm.getParentVappReference());
//		} catch (VCloudException e) {
//			e.printStackTrace();
//		}
	}
	
	public void refresh() throws AdapterException {
		try {
			ownerVapp = Vapp.getVappByReference(vcloudClient, vm.getParentVappReference());
			for (VM v : ownerVapp.getChildrenVms()) {
				if(vm.getResource().getId().equals(v.getResource().getId())) {
					vm = v;
				}
			}

		} catch (VCloudException e) {
			throw new AdapterException(e);
		}
	}

	@Override
	public void destroy() throws AdapterException {
		try {
			/**
			 * 虚拟机删除注意三点：
			 * 1.先关闭虚拟机
			 * 2.解部署undeploy，如果类型为POWERED_OFF则先poweroff或者shutdown.
			 * 3.vm对象解部署后，需要刷洗新，旧的vm对象发出请求，状态不一致，导致请求失败。
			 * 4.发出power off请求之后vm状态不会立刻生效，因此refresh状态错误
			 * 		解决方案：遍历vm状态，直至正确状态，设置超时时间；
			 */
			
			//启动状态->删除VM
			if(vm.getVMStatus() == VMStatus.POWERED_ON) {
				vm.powerOff().waitForTask(0);
				
				if(vm.isDeployed()) {
					vm.undeploy(UndeployPowerActionType.POWEROFF).waitForTask(0);;
				}
				
				int flag = 0;
				//直到状态为POWER_OFF
				while(true) {
					if(vm.getVMStatus() == VMStatus.POWERED_OFF) {
						break;
					} else {
						Thread.sleep(REFRESH_INTERVAL);
						refresh();
					}
					
					if(flag > REFRESH_NUM) {
						throw new AdapterException("Destroy vm timeout failure.");
					}
					
					flag++;
				}
				
				vm.delete().waitForTask(0);
				return;
				
			}
			
			//关机状态->删除VM
			if(vm.getVMStatus() == VMStatus.POWERED_OFF) {
				if(vm.isDeployed()) {
					vm.undeploy(UndeployPowerActionType.POWEROFF).waitForTask(0);;
				}
				vm.delete().waitForTask(0);
				return;
			}
			
			if(vm.getVMStatus() == VMStatus.SUSPENDED) {
				if(vm.isDeployed()) {
					vm.undeploy(UndeployPowerActionType.POWEROFF).waitForTask(0);;
				}
				
				refresh();
				vm.delete().waitForTask(0);
				return;
			} 
			
			vm.delete().waitForTask(0);
		} catch (VCloudException e) {
			throw new AdapterException(e);
		} catch (TimeoutException e) {
			throw new AdapterException(e);
		} catch (InterruptedException e) {
			throw new AdapterException(e);
		}

	}
	
	@Override
	public VmStatus getStatus() throws AdapterException {
		VmStatus status = VmStatus.UNKNOWN;
		
		if(vm.getVMStatus() == VMStatus.POWERED_ON) {
			status = VmStatus.RUNNING;
		} else if(vm.getVMStatus() == VMStatus.POWERED_OFF) {
			status = VmStatus.STOPPED;
		} else if(vm.getVMStatus() == VMStatus.SUSPENDED) {
			status = VmStatus.SUSPENDED;
		}
		
		return status;
	}

	@Override
	public VirtualMachineConfiguration getConfiguration() throws AdapterException {
		VirtualMachineConfiguration vmConfig = new VirtualMachineConfiguration();
		try {
			ComputeResource computeRes = new ComputeResource();
			int cpuNum = vm.getCpu().getNoOfCpus();
			int coreNumPerCpu = vm.getCpu().getCoresPerSocket();
			int memory = vm.getMemory().getMemorySize().intValue();

			computeRes.setCpuNum(cpuNum);
			computeRes.setCoreNumPerCpu(coreNumPerCpu);
			computeRes.setMemory(memory);
			vmConfig.setComputeResource(computeRes);
			long size = 0;
			List<VirtualDisk> disks = vm.getDisks();

			for (VirtualDisk d : disks) {
				if(d.isHardDisk()) {
					size += d.getHardDiskSize().longValue();
				}
				
			}
			//disk size from M TO G
			int diskSize = (int) Math.rint(size / EACH_SIZE);

			vmConfig.setDiskSize(diskSize);

			String desc = vm.getOperatingSystemSection().getDescription().getValue();
			OsInfo osInfo = new OsInfo();
			osInfo.setName(desc);
			osInfo.setPlatform(Platform.guess(desc));

			vmConfig.setOsInfo(osInfo);
		} catch (VCloudException e) {
			throw new AdapterException(e);
		}
		return vmConfig;
	}

	@Override
	public IDatacenter getDatacenter() throws AdapterException {
		VCloudDatacenter vdatac = null;
		try {
			Vdc vdc = Vdc.getVdcByReference(vcloudClient, ownerVapp.getVdcReference());
			vdatac = new VCloudDatacenter(vcloudClient, vdc);
		} catch (VCloudException e) {
			throw new AdapterException(e);
		}
		return vdatac;
	}

	@Override
	public IDatastore[] getDatastores() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHost getHost() throws AdapterException {
		return null;
	}

	@Override
	public String getName() throws AdapterException {
		if(vm != null) {
			return vm.getResource().getName();
		}
		return null;
	}
	
	@Override
	public String getId() throws AdapterException {
		if(vm != null) {
			return vm.getResource().getName();
		}
		return null;
	}

	@Override
	public INetwork[] getNetworks() throws AdapterException {
		try {
			vm.getNetworkConnectionSection();
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String getNetworkId() throws AdapterException {
		NetworkConnectionSectionType networkConnectionSection = null;
		String networkId = "";
		try {
			networkConnectionSection = vm.getNetworkConnectionSection();
			List<NetworkConnectionType> networkConnections = networkConnectionSection.getNetworkConnection();
			if (networkConnections.size() != 1) {
				throw new AdapterRuntimeException("The virtual machine " + vm.getResource().getName()
						+ "have no available network configuration.");
			}
			NetworkConnectionType networkConnection = networkConnections.get(0);
			networkId = networkConnection.getNetwork();
		} catch (VCloudException e) {
			throw new AdapterException(e);
		}
		
		return networkId;
	}

	@Override
	public OsInfo getOs() throws AdapterException {
		OsInfo osInfo = null;
		try {
			String desc = vm.getOperatingSystemSection().getDescription().getValue();
			osInfo = new OsInfo();
			osInfo.setName(desc);
			osInfo.setPlatform(Platform.guess(desc));
		} catch (VCloudException e) {
			throw new AdapterException(e);
		}
		return osInfo;
	}

	@Override
	public VirtualMachineRuntimeInfo getRuntimeInfo() throws AdapterException {
		VirtualMachineRuntimeInfo info = new VirtualMachineRuntimeInfo();
		OsInfo osInfo = null;
		try {
			String desc = vm.getOperatingSystemSection().getDescription().getValue();
			osInfo = new OsInfo();
			osInfo.setName(desc);
			osInfo.setPlatform(Platform.guess(desc));
		} catch (VCloudException e) {
			throw new AdapterException(e);
		}
		
		info.setOsInfo(osInfo);
		try {
			info.setHostName(vm.getGuestCustomizationSection().getComputerName());
			info.setPrivateIp(vm.getIpAddressesById().isEmpty() ? null : vm.getIpAddressesById().get(0));
		} catch (VCloudException e) {
			throw new AdapterException(e);
		}
		
		info.setStatus(getStatus());
		
		return info;
	}

	@Override
	public void powerOff() throws AdapterException {
		if (vm.getVMStatus() != VMStatus.POWERED_ON) {
			return;
		}
		try {
			vm.powerOff().waitForTask(0);
			
			if(vm.isDeployed()) {
				vm.undeploy(UndeployPowerActionType.POWEROFF).waitForTask(0);
			}
			
		} catch (VCloudException e) {
			throw new AdapterException(e);
		} catch (TimeoutException e) {
			throw new AdapterException(e);
		}

	}

	@Override
	public void powerOn() throws AdapterException {
		if (vm.getVMStatus() == VMStatus.POWERED_ON) {
			return;
		}
		try {
			vm.powerOn().waitForTask(0);
		} catch (VCloudException e) {
			throw new AdapterException(e);
		} catch (TimeoutException e) {
			throw new AdapterException(e);
		}

	}

	@Override
	public void reboot() throws AdapterException {
		if (vm.getVMStatus() != VMStatus.POWERED_ON) {
			throw new AdapterException("VM's status is " + vm.getVMStatus() + ", Unable to perform this action.");
		}
		
		try {
			vm.reboot().waitForTask(0);
		} catch (VCloudException e) {
			throw new AdapterException(e);
		} catch (TimeoutException e) {
			throw new AdapterException(e);
		}

	}

	@Override
	public IVirtualMachine rename(String name) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void suspend() throws AdapterException {
		try {
			vm.suspend().waitForTask(0);
		} catch (VCloudException e) {
			throw new AdapterException(e);
		} catch (TimeoutException e) {
			throw new AdapterException(e);
		}

	}

	@Override
	public void update(GuestCustomizationOptions guestCustomOptions) throws AdapterException {
		if (guestCustomOptions == null) {
			return;
		}

		// Configure hardware Guest Customization
		try {
			GuestCustomizationSectionType guestCustomizationSectionType = vm.getGuestCustomizationSection();
			guestCustomizationSectionType.setEnabled(true);
			guestCustomizationSectionType.setAdminPasswordAuto(false);
			guestCustomizationSectionType.setAdminPasswordEnabled(true);
			guestCustomizationSectionType.setAdminPassword(guestCustomOptions.getAdminPassword());

			// MsgType opsys =
			// targetVm.getOperatingSystemSection().getDescription();
			if (guestCustomOptions.getPlatform() == Platform.WINDOWS) {
				guestCustomizationSectionType.setChangeSid(true);
				guestCustomizationSectionType.setComputerName(guestCustomOptions.getComputeName());
			} else {
				guestCustomizationSectionType.setComputerName(vm.getResource().getName() + "."
						+ guestCustomOptions.getDnsSuffix());
			}

			vm.updateSection(guestCustomizationSectionType).waitForTask(0);

		} catch (VCloudException e) {
			throw new AdapterException(e);
		} catch (TimeoutException e) {
			throw new AdapterException(e);
		}
	}

	@Override
	public void update(HardwareOptions hardwareOptions) throws AdapterException {
		// Configure hardware
		try {
			VirtualCpu virtualCpu = vm.getCpu();
			virtualCpu.setNoOfCpus(hardwareOptions.getCpuNum());
			vm.updateCpu(virtualCpu).waitForTask(0);

			VirtualMemory virtualMemory = vm.getMemory();
			virtualMemory.setMemorySize(BigInteger.valueOf(hardwareOptions.getMemory()));
			vm.updateMemory(virtualMemory).waitForTask(0);
		} catch (VCloudException e) {
			throw new AdapterException(e);
		} catch (TimeoutException e) {
			throw new AdapterException(e);
		}
	}

	@Override
	public void update(LaunchVmOptions lauchVmOptions) throws AdapterException {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(NetworkConfigOptions networkConfigOptions) throws AdapterException {
		// Configure network.
		try {
			NetworkConnectionSectionType networkConnectionSection = vm.getNetworkConnectionSection();
			List<NetworkConnectionType> networkConnections = networkConnectionSection.getNetworkConnection();
			if (networkConnections.size() != 1) {
				throw new AdapterRuntimeException("The virtual machine " + vm.getResource().getName()
						+ "have no available network configuration.");
			}
			NetworkConnectionType networkConnection = networkConnections.get(0);
			networkConnection.setIpAddressAllocationMode(IpAddressAllocationModeType.MANUAL.value());
			networkConnection.setIpAddress(networkConfigOptions.getPublicIpAddress());
			networkConnection.setNetwork(networkConfigOptions.getNetworkConfigName());
			networkConnection.setIsConnected(true);
			vm.updateSection(networkConnectionSection).waitForTask(0);
		} catch (VCloudException e) {
			throw new AdapterException(e);
		} catch (TimeoutException e) {
			throw new AdapterException(e);
		}
	}

}
