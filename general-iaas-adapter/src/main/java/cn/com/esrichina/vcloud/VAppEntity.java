package cn.com.esrichina.vcloud;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.xml.bind.JAXBElement;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.AdapterRuntimeException;
import cn.com.esrichina.adapter.commons.GuestCustomizationOptions;
import cn.com.esrichina.adapter.commons.HardwareOptions;
import cn.com.esrichina.adapter.commons.ImageOptions;
import cn.com.esrichina.adapter.commons.LaunchVmOptions;
import cn.com.esrichina.adapter.commons.NetworkConfigOptions;
import cn.com.esrichina.adapter.commons.Platform;
import cn.com.esrichina.vcloud.network.NetworkConfigOfVdc;

import com.vmware.vcloud.api.rest.schema.GuestCustomizationSectionType;
import com.vmware.vcloud.api.rest.schema.InstantiationParamsType;
import com.vmware.vcloud.api.rest.schema.NetworkAssignmentType;
import com.vmware.vcloud.api.rest.schema.NetworkConnectionSectionType;
import com.vmware.vcloud.api.rest.schema.NetworkConnectionType;
import com.vmware.vcloud.api.rest.schema.ObjectFactory;
import com.vmware.vcloud.api.rest.schema.RecomposeVAppParamsType;
import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.api.rest.schema.SourcedCompositionItemParamType;
import com.vmware.vcloud.api.rest.schema.ovf.MsgType;
import com.vmware.vcloud.api.rest.schema.ovf.SectionType;
import com.vmware.vcloud.sdk.Task;
import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.VM;
import com.vmware.vcloud.sdk.Vapp;
import com.vmware.vcloud.sdk.VappTemplate;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.Vdc;
import com.vmware.vcloud.sdk.VirtualCpu;
import com.vmware.vcloud.sdk.VirtualMemory;
import com.vmware.vcloud.sdk.constants.IpAddressAllocationModeType;
import com.vmware.vcloud.sdk.constants.UndeployPowerActionType;
import com.vmware.vcloud.sdk.constants.VMStatus;
import com.vmware.vcloud.sdk.constants.VappStatus;

public class VAppEntity {

	private String vappName;
	private Vapp vapp;
	private VcloudClient vcloudClient;

	public VAppEntity(String vappName, VcloudClient vcloudClient, Vapp vapp) {
		this.vappName = vappName;
		this.vapp = vapp;
		this.vcloudClient = vcloudClient;
	}

	public String getVappName() {
		if (vappName != null) {
			return vappName;
		}
		if (vapp != null) {
			return vapp.getResource().getName();
		}
		return vappName;
	}

	public Vapp getVapp() {
		return vapp;
	}

	public void setVapp(Vapp vapp) {
		this.vapp = vapp;
	}

	public VcloudClient getVcloudClient() {
		return vcloudClient;
	}

	public void setVcloudClient(VcloudClient vcloudClient) {
		this.vcloudClient = vcloudClient;
	}

	public VdcEntity getVdc() {
		VdcEntity vdce = null;
		try {
			Vdc v = Vdc.getVdcByReference(vcloudClient, vapp.getVdcReference());
			vdce = new VdcEntity(v.getResource().getName(), vcloudClient, v);
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return vdce;
	}

	public List<VM> listVms() {
		try {
			return vapp.getChildrenVms();
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return new ArrayList<VM>();
	}

	public VM getVMbyName(String name) {
		for (VM vm : listVms()) {
			if (name.equals(vm.getResource().getName())) {
				return vm;
			}
		}
		return null;
	}

	public void createVM(String vmName, NetworkConfigOfVdc networkConfigOfVdc, VappTemplate vappTemplate) {
		VappTemplate vmTemplate = vappTemplate.getChildren().get(0);

		SourcedCompositionItemParamType vappTemplateItem = new SourcedCompositionItemParamType();
		ReferenceType vappTemplateVMRef = new ReferenceType();
		vappTemplateVMRef.setHref(vmTemplate.getReference().getHref());
		vappTemplateVMRef.setName(vmName);
		vappTemplateItem.setSource(vappTemplateVMRef);

		// When a vApp includes Vm elements that connect to networks with
		// different names, you can use a NetworkAssignment element to
		// assign the network connection for each Vm to a specific network
		// in the parent vApp
		if (vmTemplate.getNetworkConnectionSection().getNetworkConnection().size() > 0) {
			for (NetworkConnectionType networkConnection : vmTemplate.getNetworkConnectionSection()
					.getNetworkConnection()) {
				if (networkConnection.getNetworkConnectionIndex() == vmTemplate.getNetworkConnectionSection()
						.getPrimaryNetworkConnectionIndex()) {
					NetworkAssignmentType networkAssignment = new NetworkAssignmentType();
					networkAssignment.setInnerNetwork(networkConnection.getNetwork());
					networkAssignment.setContainerNetwork(networkConfigOfVdc.getNetworkConfigName());
					List<NetworkAssignmentType> networkAssignments = vappTemplateItem.getNetworkAssignment();
					networkAssignments.add(networkAssignment);
				}
			}
		}
		// If the vApp's Vm elements does not contain any network
		// connections. The network connection settings can be edited and
		// updated with the network on which you want the Vm's to connect
		// to.
		else {
			MsgType networkInfo = new MsgType();
			NetworkConnectionSectionType networkConnectionSectionType = new NetworkConnectionSectionType();
			networkConnectionSectionType.setInfo(networkInfo);

			NetworkConnectionType networkConnectionType = new NetworkConnectionType();
			networkConnectionType.setNetwork(networkConfigOfVdc.getNetworkConfigName());
			networkConnectionType.setIpAddressAllocationMode(IpAddressAllocationModeType.MANUAL.value());
			networkConnectionSectionType.getNetworkConnection().add(networkConnectionType);

			InstantiationParamsType vmInstantiationParamsType = new InstantiationParamsType();
			List<JAXBElement<? extends SectionType>> vmSections = vmInstantiationParamsType.getSection();
			vmSections.add(new ObjectFactory().createNetworkConnectionSection(networkConnectionSectionType));
			vappTemplateItem.setInstantiationParams(vmInstantiationParamsType);
		}

		// create the recompose vapp params type.
		RecomposeVAppParamsType recomposeVAppParamsType = new RecomposeVAppParamsType();
		// recomposeVAppParamsType.setLinkedClone(true);
		recomposeVAppParamsType.setDeploy(false);
		recomposeVAppParamsType.setName(vappName);

		// adding the vm item.
		List<SourcedCompositionItemParamType> newItems = recomposeVAppParamsType.getSourcedItem();
		newItems.add(vappTemplateItem);

		try {
			Task recomposeVappTask = vapp.recomposeVapp(recomposeVAppParamsType);
			recomposeVappTask.waitForTask(0);

			for (Task task : vapp.getTasks()) {
				task.waitForTask(0);
			}

			// vapp.powerOn().waitForTask(0);
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		// VappStatus vappstatus = vapp.getVappStatus();
		// System.out.println(vappstatus.name() + "   " + vappstatus.value() +
		// "   " + vappstatus.toString());
	}

	public void createVM(LaunchVmOptions launchVmOptions) throws AdapterException {

		VM targetVm = null;

		ImageOptions imageOpts = launchVmOptions.getImageOptions();
		HardwareOptions hardwareOpts = launchVmOptions.getHardwareOptions();
		NetworkConfigOptions networkOpts = launchVmOptions.getNetworkConfigOptions();
		GuestCustomizationOptions guestCustOpts = launchVmOptions.getGuestCustomOptions();

		if (imageOpts == null) {
			throw new AdapterException("no template for creating virtual machine.");
		}

		String templateName = imageOpts.getImageName();

		if (templateName == null) {
			throw new AdapterException("Do not specify template name for creating virtual machine.");
		}

		VappTemplate vmTemplate = getVdc().getVappTemplateByName(templateName).getChildren().get(0);

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
		if (vmTemplate.getNetworkConnectionSection().getNetworkConnection().size() > 0) {
			for (NetworkConnectionType networkConnection : vmTemplate.getNetworkConnectionSection()
					.getNetworkConnection()) {
				if (networkConnection.getNetworkConnectionIndex() == vmTemplate.getNetworkConnectionSection()
						.getPrimaryNetworkConnectionIndex()) {
					NetworkAssignmentType networkAssignment = new NetworkAssignmentType();
					networkAssignment.setInnerNetwork(networkConnection.getNetwork());

					networkAssignment
							.setContainerNetwork(vmTemplate.getNetworkConnectionSection().getInfo().getValue());
					List<NetworkAssignmentType> networkAssignments = vappTemplateItem.getNetworkAssignment();
					networkAssignments.add(networkAssignment);
				}
			}
		} else {
			// If the vApp's Vm elements does not contain any network
			// connections. The network connection settings can be edited and
			// updated with the network on which you want the Vm's to connect
			// to.
			MsgType networkInfo = new MsgType();
			NetworkConnectionSectionType networkConnectionSectionType = new NetworkConnectionSectionType();
			networkConnectionSectionType.setInfo(networkInfo);

			NetworkConnectionType networkConnectionType = new NetworkConnectionType();

			if (networkOpts == null || networkOpts.getNetworkConfigName() == null
					|| "".equals(networkOpts.getNetworkConfigName())) {
				throw new AdapterException("No network configuration for creating virtual machine.");
			}
			networkConnectionType.setNetwork(networkOpts.getNetworkConfigName());
			networkConnectionType.setIpAddressAllocationMode(IpAddressAllocationModeType.MANUAL.value());
			networkConnectionSectionType.getNetworkConnection().add(networkConnectionType);

			InstantiationParamsType vmInstantiationParamsType = new InstantiationParamsType();
			List<JAXBElement<? extends SectionType>> vmSections = vmInstantiationParamsType.getSection();
			vmSections.add(new ObjectFactory().createNetworkConnectionSection(networkConnectionSectionType));
			vappTemplateItem.setInstantiationParams(vmInstantiationParamsType);
		}

		// create the recompose vapp params type.
		RecomposeVAppParamsType recomposeVAppParamsType = new RecomposeVAppParamsType();
		// recomposeVAppParamsType.setLinkedClone(true);
		recomposeVAppParamsType.setDeploy(false);
		recomposeVAppParamsType.setName(vapp.getResource().getName());

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

			try {
				List<VM> vms = vapp.getChildrenVms();
				if (vms != null) {
					for (VM vm : vms) {
						if (launchVmOptions.getVmName().equals(vm.getResource().getName())) {
							targetVm = vm;
						}
					}
				}
			} catch (VCloudException e) {
				e.printStackTrace();
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

			// After Re compose success and then deploy vapp.
			vapp.deploy(true, 0, false).waitForTask(0);

		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

	}

	private void configVmhardhare(VM targetVm, HardwareOptions hardwareOpts) {
		// Configure hardware
		try {
			VirtualCpu virtualCpu = targetVm.getCpu();
			virtualCpu.setNoOfCpus(hardwareOpts.getCpuNum());
			targetVm.updateCpu(virtualCpu).waitForTask(0);

			VirtualMemory virtualMemory = targetVm.getMemory();
			virtualMemory.setMemorySize(BigInteger.valueOf(hardwareOpts.getMemory()));
			targetVm.updateMemory(virtualMemory).waitForTask(0);
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

	}

	private void configVmGuestCustom(VM targetVm, LaunchVmOptions launchVmOptions) {
		// Configure hardware Guest Customization
		try {
			GuestCustomizationSectionType guestCustomizationSectionType = targetVm.getGuestCustomizationSection();
			guestCustomizationSectionType.setEnabled(true);
			guestCustomizationSectionType.setAdminPasswordAuto(false);
			guestCustomizationSectionType.setAdminPasswordEnabled(true);
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

			// Configure network.
			NetworkConnectionSectionType networkConnectionSection = targetVm.getNetworkConnectionSection();
			List<NetworkConnectionType> networkConnections = networkConnectionSection.getNetworkConnection();
			if (networkConnections.size() != 1) {
				throw new AdapterRuntimeException("The virtual machine " + launchVmOptions.getVmName()
						+ "have no available network configuration.");
			}
			NetworkConnectionType networkConnection = networkConnections.get(0);
			networkConnection.setIpAddressAllocationMode(IpAddressAllocationModeType.MANUAL.value());
			networkConnection.setIpAddress(launchVmOptions.getNetworkConfigOptions().getPublicIpAddress());
			// networkConnection.setNetwork(launchVmOptions.getNetworkConfigOptions().getNetworkConfigId());
			networkConnection.setIsConnected(true);
			targetVm.updateSection(networkConnectionSection).waitForTask(0);

		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	// private void configVM(LaunchVmOptions options) {
	// // VMware can only add-configure virtual machine when it is running.
	// VM targetVm = getVMbyName(options.getVmName());
	//
	// if (targetVm == null) {
	// throw new AdapterRuntimeException("虚拟机" + options.getVmName() + "不存在.");
	// }
	//
	// try {
	// // Configure hardware
	// VirtualCpu virtualCpu = targetVm.getCpu();
	// virtualCpu.setNoOfCpus(options.getCpuNum());
	// targetVm.updateCpu(virtualCpu).waitForTask(0);
	//
	// VirtualMemory virtualMemory = targetVm.getMemory();
	// virtualMemory.setMemorySize(BigInteger.valueOf(options.getMemory()));
	// targetVm.updateMemory(virtualMemory).waitForTask(0);
	//
	// // Configure hardware Guest Customization
	// GuestCustomizationSectionType guestCustomizationSectionType =
	// targetVm.getGuestCustomizationSection();
	// guestCustomizationSectionType.setEnabled(true);
	// // guestCustomizationSectionType.setAdminPasswordAuto(false);
	// // guestCustomizationSectionType.setAdminPasswordEnabled(true);
	// //
	// guestCustomizationSectionType.setAdminPassword(options.getAdminPassword());
	//
	// MsgType opsys = targetVm.getOperatingSystemSection().getDescription();
	// if (options.getPlatform() == Platform.WINDOWS) {
	// guestCustomizationSectionType.setChangeSid(true);
	// guestCustomizationSectionType.setComputerName(options.getHostName());
	// } else {
	// guestCustomizationSectionType.setComputerName(options.getVmName() + "." +
	// options.getDnsSuffix());
	// }
	//
	// targetVm.updateSection(guestCustomizationSectionType).waitForTask(0);
	//
	// // Configure network.
	// NetworkConnectionSectionType networkConnectionSection =
	// targetVm.getNetworkConnectionSection();
	// List<NetworkConnectionType> networkConnections =
	// networkConnectionSection.getNetworkConnection();
	// if (networkConnections.size() != 1) {
	// throw new AdapterRuntimeException("虚拟机" + options.getVmName() +
	// "无可用网络.");
	// }
	// NetworkConnectionType networkConnection = networkConnections.get(0);
	// networkConnection.setIpAddressAllocationMode(IpAddressAllocationModeType.MANUAL.value());
	// networkConnection.setIpAddress(options.getPublicIpAddress());
	// // networkConnection.setNetwork(options.getNetworkConfigId());
	// networkConnection.setIsConnected(true);
	// targetVm.updateSection(networkConnectionSection).waitForTask(0);
	//
	// } catch (VCloudException e) {
	// e.printStackTrace();
	// } catch (TimeoutException e) {
	// e.printStackTrace();
	// }
	//
	// }

	public void updateVM(String vmname, LaunchVmOptions options) {
		VM vm = getVMbyName(vmname);
		try {
			if (vm == null) {
				throw new AdapterRuntimeException("虚拟机" + vmname + "不存在.");
			}

			if (vm.getVMStatus() == VMStatus.POWERED_ON) {
				vm.powerOff().waitForTask(0);
			}
			// configure vm hardware
			if (options.getHardwareOptions() != null) {
				configVmhardhare(vm, options.getHardwareOptions());
			}

			// Configure Guest Customization
			if (options.getGuestCustomOptions() != null) {
				configVmGuestCustom(vm, options);
			}

			// vm configuration become effective after vapp deployment
			vapp.deploy(true, 0, false).waitForTask(0);
			// vm.powerOn().waitForTask(0);
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public void setVappName(String vappName) {
		this.vappName = vappName;
	}

	public void deploy() {
		try {
			vapp.deploy(false, 1000000, false).waitForTask(0);
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	// Start vapp
	public void start() {
		if (VappStatus.POWERED_ON == vapp.getVappStatus()) {
			return;
		}

		try {
			vapp.powerOn().waitForTask(0);
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	// Stop vapp include two operations: powerOff/undeploy.
	public void stop() {
		if (VappStatus.POWERED_ON == vapp.getVappStatus()) {
			try {
				vapp.powerOff().waitForTask(0);
				vapp.undeploy(UndeployPowerActionType.POWEROFF).waitForTask(0);
			} catch (VCloudException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}

	}

	public void powerOn() {
		if (VappStatus.POWERED_ON == vapp.getVappStatus()) {
			return;
		}

		try {
			vapp.powerOn().waitForTask(0);
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public void powerOff() {
		if (VappStatus.POWERED_OFF == vapp.getVappStatus()) {
			return;
		}

		try {
			vapp.powerOff().waitForTask(0);
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {

		try {
			vapp.shutdown().waitForTask(0);
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public void suspend() {
		if (VappStatus.SUSPENDED == vapp.getVappStatus()) {
			return;
		}

		try {
			vapp.suspend().waitForTask(0);
			;
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public void undeploy() {
		if (VappStatus.POWERED_OFF == vapp.getVappStatus()) {
			try {
				vapp.undeploy(UndeployPowerActionType.POWEROFF).waitForTask(0);
			} catch (VCloudException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}

	}

	// delete vapp
	public void delete() {
		try {
			if (VappStatus.POWERED_OFF == vapp.getVappStatus()) {
				vapp.undeploy(UndeployPowerActionType.POWEROFF).waitForTask(0);
				vapp.delete().waitForTask(0);
			} else if (VappStatus.POWERED_ON == vapp.getVappStatus()) {
				vapp.powerOff().waitForTask(0);
				vapp.undeploy(UndeployPowerActionType.POWEROFF).waitForTask(0);
				vapp.delete().waitForTask(0);
			} else {
				vapp.delete().waitForTask(0);
			}
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

	}

}
