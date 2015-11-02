package cn.com.esrichina.adapter.vcloud.domain;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.AdapterRuntimeException;
import cn.com.esrichina.adapter.commons.Cpu;
import cn.com.esrichina.adapter.commons.CpuUnit;
import cn.com.esrichina.adapter.commons.DatacenterComputeRuntimeInfo;
import cn.com.esrichina.adapter.commons.DatacenterConfiguration;
import cn.com.esrichina.adapter.commons.DatacenterStorageRuntimeInfo;
import cn.com.esrichina.adapter.commons.GuestCustomizationOptions;
import cn.com.esrichina.adapter.commons.HardwareOptions;
import cn.com.esrichina.adapter.commons.ImageOptions;
import cn.com.esrichina.adapter.commons.LaunchVmOptions;
import cn.com.esrichina.adapter.commons.Memory;
import cn.com.esrichina.adapter.commons.NetworkConfigOptions;
import cn.com.esrichina.adapter.commons.Platform;
import cn.com.esrichina.adapter.commons.Storage;
import cn.com.esrichina.adapter.commons.StorageUnit;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IDatastore;
import cn.com.esrichina.adapter.domain.IHost;
import cn.com.esrichina.adapter.domain.IImage;
import cn.com.esrichina.adapter.domain.INetwork;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.domain.IVirtualMachine;
import cn.com.esrichina.adapter.feature.ClusterFeature;
import cn.com.esrichina.adapter.feature.HostFeature;
import cn.com.esrichina.adapter.feature.ImageFeature;
import cn.com.esrichina.adapter.feature.VirtualMachineFeature;
import cn.com.esrichina.adapter.vcloud.network.NetworkConfigOfVdc;

import com.vmware.vcloud.api.rest.schema.CapacityWithUsageType;
import com.vmware.vcloud.api.rest.schema.ComposeVAppParamsType;
import com.vmware.vcloud.api.rest.schema.ComputeCapacityType;
import com.vmware.vcloud.api.rest.schema.GuestCustomizationSectionType;
import com.vmware.vcloud.api.rest.schema.InstantiationParamsType;
import com.vmware.vcloud.api.rest.schema.NetworkConfigSectionType;
import com.vmware.vcloud.api.rest.schema.NetworkConfigurationType;
import com.vmware.vcloud.api.rest.schema.NetworkConnectionSectionType;
import com.vmware.vcloud.api.rest.schema.NetworkConnectionType;
import com.vmware.vcloud.api.rest.schema.ObjectFactory;
import com.vmware.vcloud.api.rest.schema.RecomposeVAppParamsType;
import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.api.rest.schema.SourcedCompositionItemParamType;
import com.vmware.vcloud.api.rest.schema.VAppNetworkConfigurationType;
import com.vmware.vcloud.api.rest.schema.VdcStorageProfilesType;
import com.vmware.vcloud.api.rest.schema.ovf.MsgType;
import com.vmware.vcloud.api.rest.schema.ovf.SectionType;
import com.vmware.vcloud.sdk.OrgVdcNetwork;
import com.vmware.vcloud.sdk.Organization;
import com.vmware.vcloud.sdk.Task;
import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.VM;
import com.vmware.vcloud.sdk.Vapp;
import com.vmware.vcloud.sdk.VappTemplate;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.Vdc;
import com.vmware.vcloud.sdk.VdcStorageProfile;
import com.vmware.vcloud.sdk.VirtualCpu;
import com.vmware.vcloud.sdk.VirtualMemory;
import com.vmware.vcloud.sdk.constants.IpAddressAllocationModeType;
import com.vmware.vcloud.sdk.constants.UndeployPowerActionType;
import com.vmware.vcloud.sdk.constants.VappStatus;

public class VCloudDatacenter implements IDatacenter {
	private Logger logger = Logger.getLogger(VCloudDatacenter.class);

	private VcloudClient vcloudClient;
	private Vdc vdc;

	public VCloudDatacenter() {

	}

	public VCloudDatacenter(VcloudClient vcloudClient, Vdc vdc) {
		this.vcloudClient = vcloudClient;
		this.vdc = vdc;
	}

	@Override
	public IOrganization getOragnization() {
		VCloudOrganization org = null;
		try {
			ReferenceType orgRef = vdc.getOrgReference();
			Organization organ = Organization.getOrganizationByReference(vcloudClient, orgRef);
			org = new VCloudOrganization(vcloudClient, organ);
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return org;
	}

	@Override
	public IHost addHost(HostFeature hostFeature, ClusterFeature clusterFeature) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IImage createImage(ImageFeature configuration) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createSite(String site, NetworkConfigOptions networkConfigOptions) throws AdapterException {
		if (networkConfigOptions == null || networkConfigOptions.getNetworkConfigName() == null
				|| "".equals(networkConfigOptions.getNetworkConfigName())) {
			throw new AdapterException("no specify network configuration name for creating vapp.");
		}
		NetworkConfigOfVdc networkconfig = (NetworkConfigOfVdc) getNetworkByName(networkConfigOptions
				.getNetworkConfigName());

		if (networkconfig == null) {
			throw new AdapterException("no available network configuration for creating vapp.");
		}
		createBlankVapp(site, networkconfig);
	}
	
	
	@Override
	public void destroySite(String siteName) throws AdapterException {
		Vapp vapp = getVappByName(siteName);
		if (vapp == null) {
			return;
		}
		
		delete(vapp);
	}

	@Override
	public IVirtualMachine createVirtualMachine(LaunchVmOptions launchVmOptions) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 虚拟机迁移过程配置一起迁移
	 * VMware vCloud 虚拟机的从一个Vapp到另一Vapp，迁移完成后自动
	 */
	@Override
	public IVirtualMachine virtualMachineMove(String vappSource, String vmName, String vappDestination) {
		try {
			Vapp vappSrc = Vapp.getVappByReference(vcloudClient, vdc.getVappRefsByName().get(vappSource));
			
			Vapp vappDest = Vapp.getVappByReference(vcloudClient, vdc.getVappRefsByName().get(vappDestination));
			VM targetVm = null;
			List<VM> vms = vappSrc.getChildrenVms();
			if (vms != null) {
				for (VM vm : vms) {
					if (vmName.equals(vm.getResource().getName())) {
						targetVm = vm;
						break;
					}
				}
			}
			
			if(targetVm == null) {
				logger.info("no have virtual machine named:" + vmName + " was moved");
				return null;
			}
			//目标虚拟机
			VCloudVirtualMachine vcvm = new VCloudVirtualMachine(vcloudClient, targetVm);
			//先关闭虚拟机
			vcvm.powerOff();
			
			SourcedCompositionItemParamType vappTemplateItem = new SourcedCompositionItemParamType();
			//ReferenceType vappTemplateVMRef = new ReferenceType();
			ReferenceType vappTemplateVMRef = targetVm.getReference();
			vappTemplateVMRef.setHref(targetVm.getReference().getHref());
			vappTemplateVMRef.setName(targetVm.getResource().getName());
			vappTemplateItem.setSource(vappTemplateVMRef);
			
			
//			MsgType networkInfo = new MsgType();
//			NetworkConnectionSectionType networkConnectionSectionType = new NetworkConnectionSectionType();
//			networkConnectionSectionType.setInfo(networkInfo);
//
//			NetworkConnectionType networkConnectionType = new NetworkConnectionType();
//
//			
//			networkConnectionType.setNetwork(vappDest.getNetworkConfigSection().getNetworkConfig().get(0).getNetworkName());
//			networkConnectionType.setIpAddressAllocationMode(IpAddressAllocationModeType.DHCP.value());
//			networkConnectionSectionType.getNetworkConnection().add(networkConnectionType);

			InstantiationParamsType vmInstantiationParamsType = new InstantiationParamsType();
//			List<JAXBElement<? extends SectionType>> vmSections = vmInstantiationParamsType.getSection();
//			vmSections.add(new ObjectFactory().createNetworkConnectionSection(networkConnectionSectionType));
			vappTemplateItem.setInstantiationParams(vmInstantiationParamsType);
			
			// create the recompose vapp params type.
			RecomposeVAppParamsType recomposeVAppParamsType = new RecomposeVAppParamsType();
			//recomposeVAppParamsType.setLinkedClone(true);
			recomposeVAppParamsType.setDeploy(false);
			recomposeVAppParamsType.setName(vappDestination);
			
			// adding the vm item.
			List<SourcedCompositionItemParamType> newItems = recomposeVAppParamsType.getSourcedItem();
			newItems.add(vappTemplateItem);
			//创建虚拟机
			try {
				Task recomposeVappTask = vappDest.recomposeVapp(recomposeVAppParamsType);
				
				recomposeVappTask.waitForTask(0);

				for (Task task : recomposeVappTask.getTasks()) {
					int pro = task.getProgress();
					task.waitForTask(0);
				}
			//删除虚拟机
			vcvm.refresh();
			vcvm.destroy();
			
			VM destVm = null;
			try {
				vappDest = Vapp.getVappByReference(vcloudClient, vdc.getVappRefsByName().get(vappDestination));
				List<VM> destVms = vappDest.getChildrenVms();
				if (vms != null) {
					for (VM vm : destVms) {
						if (vmName.equals(vm.getResource().getName())) {
							destVm = vm;
							break;
						}
					}
				}
			} catch (VCloudException e) {
				e.printStackTrace();
			}
			VCloudVirtualMachine vm = new VCloudVirtualMachine(vcloudClient, destVm);
			//destination Vapp need to deploy and do not power on.
			vappDest.deploy(false, 0, false).waitForTask(0);
			vm.powerOn();
			return vm;
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public IVirtualMachine createVirtualMachine(String siteName, LaunchVmOptions launchVmOptions)
			throws AdapterException {
		VM targetVm = null;
		Vapp vapp = getVappByName(siteName);
		if (vapp == null) {
			throw new AdapterException("no vapp for creating virtual machine.");
		}
		ImageOptions imageOpts = launchVmOptions.getImageOptions();
		HardwareOptions hardwareOpts = launchVmOptions.getHardwareOptions();
		NetworkConfigOptions networkOpts = launchVmOptions.getNetworkConfigOptions();
		GuestCustomizationOptions guestCustOpts = launchVmOptions.getGuestCustomOptions();

		if (imageOpts == null) {
			throw new AdapterException("no template for creating virtual machine.");
		}
		//VMware vcloud ImageID and ImageName are the same.
		String templateName = imageOpts.getImageId();

		if (templateName == null) {
			throw new AdapterException("Do not specify template name for creating virtual machine.");
		}

		VappTemplate vappTemplate = getTemplateByName(templateName);
		VappTemplate vmTemplate = null;
		if(vappTemplate != null) {
			vmTemplate = vappTemplate.getChildren().get(0);
		} else {
			throw new AdapterException("No vapp template for creating virtual machine.");
		}
		
		SourcedCompositionItemParamType vappTemplateItem = new SourcedCompositionItemParamType();
		ReferenceType vappTemplateVMRef = new ReferenceType();
		vappTemplateVMRef.setHref(vmTemplate.getReference().getHref());
		vappTemplateVMRef.setName(launchVmOptions.getVmName());
		vappTemplateItem.setSource(vappTemplateVMRef);

		// Following for Handling Network Configuration
		// When a vApp includes Vm elements that connect to networks with
		// different names, you can use a NetworkAssignment element to
		// assign the network connection for each Vm to a specific network
		// in the parent vApp
//		if (vmTemplate.getNetworkConnectionSection().getNetworkConnection().size() > 0) {
//			for (NetworkConnectionType networkConnection : vmTemplate.getNetworkConnectionSection()
//					.getNetworkConnection()) {
//				if (networkConnection.getNetworkConnectionIndex() == vmTemplate.getNetworkConnectionSection()
//						.getPrimaryNetworkConnectionIndex()) {
//					NetworkAssignmentType networkAssignment = new NetworkAssignmentType();
//					networkAssignment.setInnerNetwork(networkConnection.getNetwork());
//
//					networkAssignment
//							.setContainerNetwork(vmTemplate.getNetworkConnectionSection().getInfo().getValue());
//					List<NetworkAssignmentType> networkAssignments = vappTemplateItem.getNetworkAssignment();
//					networkAssignments.add(networkAssignment);
//				}
//			}
//		} else {
//			// If the vApp's Vm elements does not contain any network
//			// connections. The network connection settings can be edited and
//			// updated with the network on which you want the Vm's to connect
//			// to.
//			MsgType networkInfo = new MsgType();
//			NetworkConnectionSectionType networkConnectionSectionType = new NetworkConnectionSectionType();
//			networkConnectionSectionType.setInfo(networkInfo);
//
//			NetworkConnectionType networkConnectionType = new NetworkConnectionType();
//
//			if (networkOpts == null || networkOpts.getNetworkConfigName() == null
//					|| "".equals(networkOpts.getNetworkConfigName())) {
//				throw new AdapterException("No network configuration for creating virtual machine.");
//			}
//			networkConnectionType.setNetwork(networkOpts.getNetworkConfigName());
//			networkConnectionType.setIpAddressAllocationMode(IpAddressAllocationModeType.MANUAL.value());
//			networkConnectionSectionType.getNetworkConnection().add(networkConnectionType);
//
//			InstantiationParamsType vmInstantiationParamsType = new InstantiationParamsType();
//			List<JAXBElement<? extends SectionType>> vmSections = vmInstantiationParamsType.getSection();
//			vmSections.add(new ObjectFactory().createNetworkConnectionSection(networkConnectionSectionType));
//			vappTemplateItem.setInstantiationParams(vmInstantiationParamsType);
//		}
		
		MsgType networkInfo = new MsgType();
		NetworkConnectionSectionType networkConnectionSectionType = new NetworkConnectionSectionType();
		networkConnectionSectionType.setInfo(networkInfo);

		NetworkConnectionType networkConnectionType = new NetworkConnectionType();

		if (networkOpts == null || networkOpts.getNetworkConfigId() == null
				|| "".equals(networkOpts.getNetworkConfigId())) {
			throw new AdapterException("No network configuration for creating virtual machine.");
		}
		networkConnectionType.setNetwork(networkOpts.getNetworkConfigId());
		networkConnectionType.setIpAddressAllocationMode(IpAddressAllocationModeType.DHCP.value());
		networkConnectionSectionType.getNetworkConnection().add(networkConnectionType);

		InstantiationParamsType vmInstantiationParamsType = new InstantiationParamsType();
		List<JAXBElement<? extends SectionType>> vmSections = vmInstantiationParamsType.getSection();
		vmSections.add(new ObjectFactory().createNetworkConnectionSection(networkConnectionSectionType));
		vappTemplateItem.setInstantiationParams(vmInstantiationParamsType);

		// create the recompose vapp params type.
		RecomposeVAppParamsType recomposeVAppParamsType = new RecomposeVAppParamsType();
		// recomposeVAppParamsType.setLinkedClone(true);
		recomposeVAppParamsType.setDeploy(false);
		recomposeVAppParamsType.setName(siteName);

		// adding the vm item.
		List<SourcedCompositionItemParamType> newItems = recomposeVAppParamsType.getSourcedItem();
		newItems.add(vappTemplateItem);

		try {
			Task recomposeVappTask = vapp.recomposeVapp(recomposeVAppParamsType);
			recomposeVappTask.waitForTask(0);

			for (Task task : recomposeVappTask.getTasks()) {
				int pro = task.getProgress();
				task.waitForTask(0);
			}
			vapp = Vapp.getVappByReference(vcloudClient, vapp.getReference());

			if (launchVmOptions.getVmName() == null || "".equals(launchVmOptions.getVmName())) {
				throw new AdapterException("Do not specify the virtual machine name.");
			}
			
			vapp = Vapp.getVappByReference(vcloudClient, vapp.getReference());
			try {
				List<VM> vms = vapp.getChildrenVms();
				if (vms != null) {
					for (VM vm : vms) {
						if (launchVmOptions.getVmName().equals(vm.getResource().getName())) {
							targetVm = vm;
							break;
						}
					}
				}
			} catch (VCloudException e) {
				throw new AdapterException(e);
			}

			if (targetVm == null) {
				throw new AdapterException("The virtual machine " + launchVmOptions.getVmName() + " not exist.");
			}

			// configure vm hardware
			if (hardwareOpts != null) {
				configVmhardhare(targetVm, hardwareOpts);
			}

			// Configure Guest Customization
			if (guestCustOpts != null) {
				configVmGuestCustom(targetVm, launchVmOptions);
			}

			// Configure Guest Customization
			if (networkOpts != null) {
				configVmNetwork(targetVm, networkOpts);
			}
			//vapp = getVappByName(siteName);
			// After Re compose success and then deploy vapp.
			vapp.deploy(false, 0, false).waitForTask(0);

		} catch (VCloudException e) {
			throw new AdapterException(e);
		} catch (TimeoutException e) {
			throw new AdapterException(e);
		}
		VCloudVirtualMachine vm = new VCloudVirtualMachine(vcloudClient, targetVm);
		//创建虚拟机完成后启动，此时不影响Vapp中的其他虚拟机
		vm.powerOn();
		return vm;
	}

	private void configVmhardhare(VM targetVm, HardwareOptions hardwareOpts) throws AdapterException {
		// Configure hardware
		try {
			VirtualCpu virtualCpu = targetVm.getCpu();
			virtualCpu.setNoOfCpus(hardwareOpts.getCpuNum());
			targetVm.updateCpu(virtualCpu).waitForTask(0);

			VirtualMemory virtualMemory = targetVm.getMemory();
			virtualMemory.setMemorySize(BigInteger.valueOf(hardwareOpts.getMemory()));
			targetVm.updateMemory(virtualMemory).waitForTask(0);
		} catch (VCloudException e) {
			throw new AdapterException(e);
		} catch (TimeoutException e) {
			throw new AdapterException(e);
		}

	}

	private void configVmGuestCustom(VM targetVm, LaunchVmOptions launchVmOptions) {
		// Configure hardware Guest Customization
		try {
			GuestCustomizationSectionType guestCustomizationSectionType = targetVm.getGuestCustomizationSection();
			guestCustomizationSectionType.setEnabled(true);
			guestCustomizationSectionType.setAdminPasswordAuto(false);
			guestCustomizationSectionType.setAdminPasswordEnabled(false);
			guestCustomizationSectionType.setAdminPassword(launchVmOptions.getGuestCustomOptions().getAdminPassword());

			// MsgType opsys =
			// targetVm.getOperatingSystemSection().getDescription();
			if (launchVmOptions.getImageOptions().getPlatform() == Platform.WINDOWS) {
				guestCustomizationSectionType.setChangeSid(true);
				guestCustomizationSectionType.setComputerName(launchVmOptions.getGuestCustomOptions().getComputeName());
			} else {
				guestCustomizationSectionType.setComputerName(launchVmOptions.getVmName() + "."
						+ launchVmOptions.getNetworkConfigOptions().getDnsSuffix());
			}

			targetVm.updateSection(guestCustomizationSectionType).waitForTask(0);

		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	private void configVmNetwork(VM targetVm, NetworkConfigOptions networkConfigOpts) {
		// Configure network.
		try {
			NetworkConnectionSectionType networkConnectionSection = targetVm.getNetworkConnectionSection();
			List<NetworkConnectionType> networkConnections = networkConnectionSection.getNetworkConnection();
			if (networkConnections.size() != 1) {
				throw new AdapterRuntimeException("The virtual machine " + targetVm.getResource().getName()
						+ "have no available network configuration.");
			}
			NetworkConnectionType networkConnection = networkConnections.get(0);
			networkConnection.setIpAddressAllocationMode(IpAddressAllocationModeType.MANUAL.value());
			networkConnection.setIpAddress(networkConfigOpts.getPrivateIpAddress());
			// networkConnection.setNetwork(launchVmOptions.getNetworkConfigOptions().getNetworkConfigId());
			networkConnection.setIsConnected(true);
			targetVm.updateSection(networkConnectionSection).waitForTask(0);
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyHost(String name) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyHosts(String[] names) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyImage(String name) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyImages(String[] names) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyVirtualMachine(String name) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyVirtualMachine(String name, VirtualMachineFeature vmFeature) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyVirtualMachines(String[] names) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public DatacenterConfiguration getConfiguration() throws AdapterException {
//		CapacityWithUsageType cap = vdc.getResource().getStorageCapacity();
//		Collection<ReferenceType> diskRefs = vdc.getDiskRefs();
//		VdcStorageProfilesType vdcStorage = vdc.getResource().getVdcStorageProfiles();
//		
//		List<ReferenceType> refs = vdcStorage.getVdcStorageProfile();
//		for(ReferenceType type: refs) {
//			try {
//				VdcStorageProfile storageProfile = VdcStorageProfile.getVdcStorageProfileByReference(vcloudClient, type);
//				String resType = storageProfile.getResource().getType();
//				long limitStorage = storageProfile.getResource().getLimit();
//				String storageUnit = storageProfile.getResource().getUnits();
//				
//				ProviderVdcStorageProfile pvsp = ProviderVdcStorageProfile.getProviderVdcStorageProfileByReference(vcloudClient, type);
//				double storageTotal = pvsp.getResource().getCapacityTotal();
//				double storageUsed = pvsp.getResource().getCapacityUsed();
//				String units = pvsp.getResource().getUnits();
//				System.out.println(storageTotal + " " + storageUsed + " " + units);
//				
//			} catch (VCloudException e) {
//				throw new AdapterException(e);
//			}
//		}
//		
//		ComputeCapacityType compCapType = vdc.getResource().getComputeCapacity();
//		CapacityWithUsageType cpu = compCapType.getCpu();
//		CapacityWithUsageType mem = compCapType.getMemory();
//		long allocatedMem = mem.getAllocated();
//		long usedMem = mem.getUsed();
//		long limitMem = mem.getLimit();
//		
//		long allocatedCput = cpu.getAllocated();
//		long used = cpu.getUsed();
//		long limit = cpu.getLimit();
//		long reserved = cpu.getReserved();
//		String unit = cpu.getUnits();
//		System.out.println(compCapType);
//		
//		Collection<ReferenceType> disks =vdc.getDiskRefs();
//		for(ReferenceType d: disks) {
//			System.out.println(d);
//		}
		
		
		return null;
	}
	
	@Override
	public DatacenterComputeRuntimeInfo getDatacenterComputeRuntimeInfo() throws AdapterException {
		ComputeCapacityType compCapType = vdc.getResource().getComputeCapacity();
		DatacenterComputeRuntimeInfo dcRtInfo = null;
		
		if(compCapType != null) {
			dcRtInfo = new DatacenterComputeRuntimeInfo();
			//CPU
			CapacityWithUsageType cpuUsed = compCapType.getCpu();
			if("MHz".equalsIgnoreCase(cpuUsed.getUnits())) {
				dcRtInfo.setTotalCpu(new Cpu(cpuUsed.getLimit(), CpuUnit.FREQUENCY_MHZ));
				dcRtInfo.setUsedCpu(new Cpu(cpuUsed.getUsed(), CpuUnit.FREQUENCY_MHZ));
			} else if("GHz".equalsIgnoreCase(cpuUsed.getUnits())) {
				dcRtInfo.setTotalCpu(new Cpu(cpuUsed.getLimit(), CpuUnit.FREQUENCY_GHZ));
				dcRtInfo.setUsedCpu(new Cpu(cpuUsed.getUsed(), CpuUnit.FREQUENCY_GHZ));
			}
			
			//Memory
			CapacityWithUsageType memoryUsed = compCapType.getMemory();
			if("GB".equalsIgnoreCase(memoryUsed.getUnits())) {
				dcRtInfo.setTotalMemory(new Memory(memoryUsed.getLimit(), StorageUnit.GIGABYTES));
				dcRtInfo.setUsedMemory(new Memory(memoryUsed.getUsed(), StorageUnit.GIGABYTES));
			} else if("MB".equalsIgnoreCase(memoryUsed.getUnits())) {
				dcRtInfo.setTotalMemory(new Memory(memoryUsed.getLimit(), StorageUnit.MEGABYTES));
				dcRtInfo.setUsedMemory(new Memory(memoryUsed.getUsed(), StorageUnit.MEGABYTES));
			}
			
			//限制均为0，则不受限。
			if(cpuUsed.getLimit() == 0L && memoryUsed.getLimit() == 0L) {
				dcRtInfo.setLimited(false);
			}
			
		}
				
		return dcRtInfo;
	}
	
	@Override
	public List<DatacenterStorageRuntimeInfo> getDatacenterStorageRuntimeInfo() throws AdapterException {
		
		List<DatacenterStorageRuntimeInfo> dcStorageInfos = new ArrayList<DatacenterStorageRuntimeInfo>();
		
		VdcStorageProfilesType vdcStorage = vdc.getResource().getVdcStorageProfiles();
		//Collection<ReferenceType> diskRefs =  vdc.getDiskRefs();
		
		List<ReferenceType> refs = vdcStorage.getVdcStorageProfile();
		
		for(ReferenceType type: refs) {
			DatacenterStorageRuntimeInfo info = new DatacenterStorageRuntimeInfo();
			try {
				//vCloud 组织VDC存储接口可能存在问题，不能获取正确的存储数据
				//处理方案：总数和使用数设置为0GB
				VdcStorageProfile storageProfile = VdcStorageProfile.getVdcStorageProfileByReference(vcloudClient, type);
				
				long limitStorage = storageProfile.getResource().getLimit();
				String storageUnit = storageProfile.getResource().getUnits();
				String storageName = storageProfile.getResource().getName();
				//没有已用存储接口getUsed();
				//long usedStorage = storageProfile.getResource().getUsed();
				
				
//				ProviderVdcStorageProfile pvsp = ProviderVdcStorageProfile.getProviderVdcStorageProfileByReference(vcloudClient, type);
//				double storageTotal = pvsp.getResource().getCapacityTotal();
//				double storageUsed = pvsp.getResource().getCapacityUsed();
//				String storageName = pvsp.getResource().getName();
//				String units = pvsp.getResource().getUnits();
//				
//				if("MB".equalsIgnoreCase(units)) {
//					
//					info.setTotal(new Storage(Double.doubleToLongBits(storageTotal), StorageUnit.MEGABYTES));
//					info.setUsed(new Storage(Double.doubleToLongBits(storageUsed), StorageUnit.MEGABYTES));
//				} else if("GB".equalsIgnoreCase(units)) {
//					
//					info.setTotal(new Storage(Double.doubleToLongBits(storageTotal), StorageUnit.GIGABYTES));
//					info.setUsed(new Storage(Double.doubleToLongBits(storageUsed), StorageUnit.GIGABYTES));
//				}
				
				info.setTotal(new Storage(0L, StorageUnit.GIGABYTES));
				info.setUsed(new Storage(0L, StorageUnit.GIGABYTES));
				
				info.setStorageName(storageName);
				
				if(limitStorage == 0L) {
					info.setLimited(false);
				}
 				dcStorageInfos.add(info);
				
			} catch (VCloudException e) {
				throw new AdapterException(e);
			}
		}
		return dcStorageInfos;
	}

	@Override
	public IDatastore[] getDatastores() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHost getHost(String hostId) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHost[] getHosts() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() throws AdapterException {
		return vdc.getResource().getName();
	}

	@Override
	public IImage getImage(String imageId) throws AdapterException {
		if (imageId == null) {
			throw new AdapterException("Image name cannot be null.");
		}

		for (IImage vcImg : getImages()) {
			if (imageId.equals(vcImg.getName())) {
				return vcImg;
			}
		}
		return null;
	}

	@Override
	public IImage[] getImages() throws AdapterException {
		List<VCloudImage> vappTemplates = new ArrayList<VCloudImage>();
		for (ReferenceType vappTemplateRef : vdc.getVappTemplateRefs()) {
			VappTemplate temp;
			try {
				temp = VappTemplate.getVappTemplateByReference(vcloudClient, vappTemplateRef);
				/**
				 * 过滤存储租约过期的模板：
				 * 处理方式:存储租约过期时间与当前时间做比较；
				 * 存在的风险：如果本系统时间与vcloud系统时间不一致，可能会产生bug,云GIS套件在虚拟化或云平台时间保持一致，
				 * 如NTP保证系统时钟同步；
				 */
				XMLGregorianCalendar xmlExpirationTime = temp.getLeaseSettingsSection().getStorageLeaseExpiration();
				
				if(null != xmlExpirationTime) {
					
					int year = xmlExpirationTime.getYear();
					int month =xmlExpirationTime.getMonth();
					int day = xmlExpirationTime.getDay();
					int hour = xmlExpirationTime.getHour();
					int minute = xmlExpirationTime.getMinute();
					int second = xmlExpirationTime.getSecond();
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
					Date expirationTime = df.parse("" + year + "-" + month + "-" + day + " " + hour +":" + minute + ":" + second);
					
					if(expirationTime.after(new Date())) {
						VCloudImage vctemp = new VCloudImage(vcloudClient, temp);
						vappTemplates.add(vctemp);
					};
					
				} else {
					
					VCloudImage vctemp = new VCloudImage(vcloudClient, temp);
					vappTemplates.add(vctemp);
				}
				
			} catch (VCloudException e) {
				throw new AdapterException(e);
			} catch (ParseException e) {
				throw new AdapterException(e);
			}

		}
		VCloudImage[] vcImgs = new VCloudImage[vappTemplates.size()];
		int i = 0;
		for (VCloudImage temp : vappTemplates) {
			vcImgs[i] = temp;
			i++;
		}
		return vcImgs;
	}

	@Override
	public String getName() throws AdapterException {
		return vdc.getResource().getName();
	}

//	@Override
//	public INetwork[] getNetworks() throws AdapterException {
//		List<NetworkConfigOfVdc> networkConfigOfVdcs = new ArrayList<NetworkConfigOfVdc>();
//		@SuppressWarnings("deprecation")
//		HashMap<String, ReferenceType> availableNetworkRefs = vdc.getAvailableNetworkRefsByName();
//		for (String networkName : availableNetworkRefs.keySet()) {
//			try {
//				NetworkConfigOfVdc networkConfigOfVdc = new NetworkConfigOfVdc(networkName, vcloudClient,
//						OrgVdcNetwork.getOrgVdcNetworkByReference(vcloudClient, availableNetworkRefs.get(networkName)));
//				networkConfigOfVdcs.add(networkConfigOfVdc);
//			} catch (VCloudException e) {
//				e.printStackTrace();
//			}
//
//		}
//
//		NetworkConfigOfVdc[] networkConfigs = new NetworkConfigOfVdc[networkConfigOfVdcs.size()];
//		int i = 0;
//		for (NetworkConfigOfVdc n : networkConfigOfVdcs) {
//			networkConfigs[i] = n;
//			i++;
//		}
//		return networkConfigs;
//	}
	
	@Override
	public INetwork[] getNetworks() throws AdapterException {
		List<NetworkConfigOfVdc> networkConfigOfVdcs = new ArrayList<NetworkConfigOfVdc>();
		@SuppressWarnings("deprecation")		
		Collection<ReferenceType> ankRefs = vdc.getAvailableNetworkRefs();
		
		for(ReferenceType rt : ankRefs) {
			try {
				OrgVdcNetwork ovnk = OrgVdcNetwork.getOrgVdcNetworkByReference(vcloudClient, rt);
				
				NetworkConfigOfVdc networkConfigOfVdc = new NetworkConfigOfVdc(ovnk.getResource().getName(), vcloudClient, ovnk);
				networkConfigOfVdcs.add(networkConfigOfVdc);
			} catch (VCloudException e) {
				e.printStackTrace();
			}
		}

		NetworkConfigOfVdc[] networkConfigs = new NetworkConfigOfVdc[networkConfigOfVdcs.size()];
		int i = 0;
		for (NetworkConfigOfVdc n : networkConfigOfVdcs) {
			networkConfigs[i] = n;
			i++;
		}
		return networkConfigs;
	}

	@Override
	public INetwork getNetworkByName(String networkName) throws AdapterException {
		if (networkName == null) {
			throw new AdapterException("network configuration name cannot be null.");
		}
		for (INetwork n : getNetworks()) {
			if (networkName.equals(n.getName())) {
				return n;
			}
		}
		return null;
	}

	@Override
	public IVirtualMachine getVirtualMachine(String vmName) throws AdapterException {
		if (vmName == null) {
			throw new AdapterException("virtual machine name cannot be null.");
		}

		for (IVirtualMachine vm : getVirtualMachines()) {
			if (vmName.equals(vm.getName())) {
				return vm;
			}
		}
		return null;
	}

	@Override
	public IVirtualMachine[] getVirtualMachines() throws AdapterException {
		List<VCloudVirtualMachine> vms = new ArrayList<VCloudVirtualMachine>();
		for (Vapp vapp : listVapps()) {
			VCloudVirtualMachine[] virtualmachines = getVmsFromVapp(vapp);
			vms.addAll(Arrays.asList(virtualmachines));
		}

		VCloudVirtualMachine[] vcvms = new VCloudVirtualMachine[vms.size()];
		int i = 0;
		for (VCloudVirtualMachine vm : vms) {
			vcvms[i] = vm;
			i++;
		}
		return vcvms;
	}

	@Override
	public IVirtualMachine[] getVirtualMachines(String site) throws AdapterException {
		Vapp vapp = getVappByName(site);
		VCloudVirtualMachine[] virtualmachines = null;
		if (vapp != null) {
			virtualmachines = getVmsFromVapp(vapp);
		}
		return virtualmachines;
	}

	@Override
	public IVirtualMachine getVirtualMachine(String site, String vmName) throws AdapterException {
		if (vmName == null) {
			throw new AdapterException("virtual machine name cannot be null.");
		}

		IVirtualMachine[] vms = getVirtualMachines(site);

		if (vms != null) {
			for (IVirtualMachine vm : vms) {
				if (vmName.equals(vm.getName())) {
					return vm;
				}
			}
		}
		return null;
	}
	
	@Override
	public IVirtualMachine getVirtualMachineById(String vmId)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IVirtualMachine getVirtualMachineByName(String vmName)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDatacenter rename(String name) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IImage renameImage(String oldName, String newName) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVirtualMachine renameVirtualMachine(String oldName, String newName) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Vapp> listVapps() {
		List<Vapp> vapps = new ArrayList<Vapp>();
		try {
			for (ReferenceType vAppRef : Vdc.getVdcByReference(vcloudClient, vdc.getReference()).getVappRefs()) {
				Vapp vapp = Vapp.getVappByReference(vcloudClient, vAppRef);
				vapps.add(vapp);
			}
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return vapps;
	}

	private Vapp getVappByName(String vappName) {
		for (Vapp vapp : listVapps()) {
			if (vappName.equals(vapp.getResource().getName())) {
				return vapp;
			}

		}
		return null;
	}

	private VCloudVirtualMachine[] getVmsFromVapp(Vapp vapp) throws AdapterException {
		VCloudVirtualMachine[] virtualmachines = null;
		try {
			List<VM> vms = vapp.getChildrenVms();
			if (vms != null) {
				virtualmachines = new VCloudVirtualMachine[vms.size()];
				int i = 0;
				for (VM vm : vms) {
					VCloudVirtualMachine vcvm = new VCloudVirtualMachine(vcloudClient, vm);
					virtualmachines[i] = vcvm;
					i++;
				}
			}
		} catch (VCloudException e) {
			throw new AdapterException(e);
		}
		return virtualmachines;
	}

	/**
	 * createBlankVapp
	 * 
	 * @param vappName
	 * @param networkConfigOfVdc
	 * @throws AdapterException 
	 */
	@SuppressWarnings("deprecation")
	private void createBlankVapp(String vappName, NetworkConfigOfVdc networkConfigOfVdc) throws AdapterException {

		NetworkConfigurationType networkConfigurationType = networkConfigOfVdc.getOrgVdcNetwork().getConfiguration();
		networkConfigurationType.setParentNetwork(vdc.getAvailableNetworkRefByName(networkConfigOfVdc
				.getNetworkConfigName()));

		VAppNetworkConfigurationType vAppNetworkConfigurationType = new VAppNetworkConfigurationType();
		vAppNetworkConfigurationType.setConfiguration(networkConfigurationType);
		vAppNetworkConfigurationType.setNetworkName(networkConfigOfVdc.getNetworkConfigName());

		NetworkConfigSectionType networkConfigSectionType = new NetworkConfigSectionType();
		MsgType networkInfo = new MsgType();
		networkConfigSectionType.setInfo(networkInfo);

		List<VAppNetworkConfigurationType> vAppNetworkConfigs = networkConfigSectionType.getNetworkConfig();
		vAppNetworkConfigs.add(vAppNetworkConfigurationType);

		InstantiationParamsType vappOrvAppTemplateInstantiationParamsType = new InstantiationParamsType();
		List<JAXBElement<? extends SectionType>> vappSections = vappOrvAppTemplateInstantiationParamsType.getSection();
		vappSections.add(new ObjectFactory().createNetworkConfigSection(networkConfigSectionType));

		ComposeVAppParamsType composeVAppParamsType = new ComposeVAppParamsType();
		composeVAppParamsType.setDeploy(false);
		composeVAppParamsType.setInstantiationParams(vappOrvAppTemplateInstantiationParamsType);
		composeVAppParamsType.setName(vappName);

		try {
			Vapp vapp = vdc.composeVapp(composeVAppParamsType);
			for (Task task : vapp.getTasks()) {
				task.waitForTask(0);
				;
			}
		} catch (VCloudException e) {
			throw new AdapterException(e);
		} catch (TimeoutException e) {
			throw new AdapterException(e);
		}
	}

	private VappTemplate getTemplateByName(String templateName) throws AdapterException {

		for (ReferenceType vappTemplateRef : vdc.getVappTemplateRefs()) {
			VappTemplate temp;
			try {
				temp = VappTemplate.getVappTemplateByReference(vcloudClient, vappTemplateRef);
				if (templateName.equals(vappTemplateRef.getName())) {
					return temp;
				}

			} catch (VCloudException e) {
				throw new AdapterException(e);
			}

		}
		return null;
	}
	
	/**
	 * vapp删除与vm删除类似
	 * 判断vapp.isDeployed是否经过部署，如果部署过，则先解部署
	 * @param vapp
	 * @throws AdapterException 
	 */
	public void delete(Vapp vapp) throws AdapterException {
		try {
 			if (VappStatus.POWERED_OFF == vapp.getVappStatus()) {
				vapp.delete().waitForTask(0);
			} else if (VappStatus.POWERED_ON == vapp.getVappStatus()) {
				vapp.powerOff().waitForTask(0);
				if(vapp.isDeployed()) {
					vapp.undeploy(UndeployPowerActionType.POWEROFF).waitForTask(0);
				}
				//延迟3s再删除
				Thread.sleep(3000L);
				vapp.delete().waitForTask(0);
			} else {
				if(vapp.isDeployed()) {
					vapp.undeploy(UndeployPowerActionType.POWEROFF).waitForTask(0);
					Thread.sleep(3000L);
				}
				vapp.delete().waitForTask(0);
			}
		} catch (VCloudException e) {
			throw new AdapterException(e);
		} catch (TimeoutException e) {
			throw new AdapterException(e);
		} catch (InterruptedException e) {
			throw new AdapterException(e);
		}

	}

}
