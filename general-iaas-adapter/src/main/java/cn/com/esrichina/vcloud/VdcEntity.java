package cn.com.esrichina.vcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.xml.bind.JAXBElement;

import cn.com.esrichina.vcloud.network.NetworkConfigOfVdc;

import com.vmware.vcloud.api.rest.schema.ComposeVAppParamsType;
import com.vmware.vcloud.api.rest.schema.InstantiationParamsType;
import com.vmware.vcloud.api.rest.schema.NetworkAssignmentType;
import com.vmware.vcloud.api.rest.schema.NetworkConfigSectionType;
import com.vmware.vcloud.api.rest.schema.NetworkConfigurationType;
import com.vmware.vcloud.api.rest.schema.NetworkConnectionSectionType;
import com.vmware.vcloud.api.rest.schema.NetworkConnectionType;
import com.vmware.vcloud.api.rest.schema.ObjectFactory;
import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.api.rest.schema.SourcedCompositionItemParamType;
import com.vmware.vcloud.api.rest.schema.VAppNetworkConfigurationType;
import com.vmware.vcloud.api.rest.schema.ovf.MsgType;
import com.vmware.vcloud.api.rest.schema.ovf.SectionType;
import com.vmware.vcloud.sdk.OrgVdcNetwork;
import com.vmware.vcloud.sdk.Organization;
import com.vmware.vcloud.sdk.Task;
import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.Vapp;
import com.vmware.vcloud.sdk.VappTemplate;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.Vdc;
import com.vmware.vcloud.sdk.constants.FenceModeValuesType;
import com.vmware.vcloud.sdk.constants.IpAddressAllocationModeType;

public class VdcEntity {

	private String vdcName;
	private VcloudClient vcloudClient;
	private List<VAppEntity> vapps = new ArrayList<VAppEntity>();

	private Vdc vdc;
	private List<NetworkConfigOfVdc> networkConfigOfVdcs = new ArrayList<NetworkConfigOfVdc>();

	public VdcEntity() {

	}

	public VdcEntity(String vdcName, VcloudClient vcloudClient, Vdc vdc) {
		this.vdcName = vdcName;
		this.vdc = vdc;
		this.vcloudClient = vcloudClient;
	}

	public OrgEntity getOrg() {
		OrgEntity org = null;
		try {
			ReferenceType orgRef = vdc.getOrgReference();
			Organization organ = Organization.getOrganizationByReference(vcloudClient, orgRef);
			org = new OrgEntity(vcloudClient, organ);
		} catch (VCloudException e) {
			e.printStackTrace();
		}

		return org;
	}

	public List<VAppEntity> listVapps() {
		try {
			for (ReferenceType vAppRef : Vdc.getVdcByReference(vcloudClient, vdc.getReference()).getVappRefs()) {
				Vapp vapp = Vapp.getVappByReference(vcloudClient, vAppRef);
				VAppEntity vae = new VAppEntity(vapp.getResource().getName(), vcloudClient, vapp);
				vapps.add(vae);
			}
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return vapps;
	}

	public VAppEntity getVappByName(String vappName) {
		VAppEntity vappEntity = null;
		try {
			for (ReferenceType vAppRef : Vdc.getVdcByReference(vcloudClient, vdc.getReference()).getVappRefs()) {
				if (vappName.equals(vAppRef.getName())) {
					Vapp vapp = Vapp.getVappByReference(vcloudClient, vAppRef);
					vappEntity = new VAppEntity(vapp.getResource().getName(), vcloudClient, vapp);
				}

			}
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return vappEntity;
	}

	public List<VappTemplate> listVappTemplates() {
		List<VappTemplate> vappTemplates = new ArrayList<VappTemplate>();
		for (ReferenceType vappTemplateRef : vdc.getVappTemplateRefs()) {
			VappTemplate temp;
			try {
				temp = VappTemplate.getVappTemplateByReference(vcloudClient, vappTemplateRef);
				vappTemplates.add(temp);
			} catch (VCloudException e) {
				e.printStackTrace();
			}

		}
		return vappTemplates;

	}

	public VappTemplate getVappTemplateByName(String tempName) {
		for (ReferenceType vappTemplateRef : vdc.getVappTemplateRefs())
			if (vappTemplateRef.getName().equals(tempName)) {
				try {
					return VappTemplate.getVappTemplateByReference(vcloudClient, vappTemplateRef);
				} catch (VCloudException e) {
					e.printStackTrace();
				}
			}
		return null;

	}

	/**
	 * createBlankVapp
	 * @param vappName
	 * @param networkConfigOfVdc
	 */
	@SuppressWarnings("deprecation")
	public void createBlankVapp(String vappName, NetworkConfigOfVdc networkConfigOfVdc) {

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
				task.waitForTask(0);;
			}
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} 
	}

	public void createVapp(String vappName, NetworkConfigOfVdc networkConfigOfVdc, VappDeployConfig deployConfig) {

		VAppNetworkConfigurationType vAppNetworkConfigurationType = new VAppNetworkConfigurationType();
		vAppNetworkConfigurationType.setConfiguration(networkConfigOfVdc.getOrgVdcNetwork().getConfiguration());
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
		List<SourcedCompositionItemParamType> items = composeVAppParamsType.getSourcedItem();

		for (VappTemplate temp : deployConfig.getConfig().keySet()) {
			VappTemplate template = temp.getChildren().get(0);
			Integer num = deployConfig.getConfig().get(temp);
			for (int i = 0; i < num.intValue(); i++) {
				SourcedCompositionItemParamType vappTemplateItem = new SourcedCompositionItemParamType();
				ReferenceType vappTemplateVMRef = new ReferenceType();
				vappTemplateVMRef.setHref(template.getReference().getHref());
				vappTemplateVMRef.setName(i + "-" + template.getReference().getName());
				vappTemplateItem.setSource(vappTemplateVMRef);

				// When a vApp includes Vm elements that connect to networks
				// with
				// different names, you can use a NetworkAssignment element to
				// assign the network connection for each Vm to a specific
				// network
				// in the parent vApp
				if (template.getNetworkConnectionSection().getNetworkConnection().size() > 0) {
					for (NetworkConnectionType networkConnection : template.getNetworkConnectionSection()
							.getNetworkConnection()) {
						if (networkConnection.getNetworkConnectionIndex() == template.getNetworkConnectionSection()
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
				// connections. The network connection settings can be edited
				// and
				// updated with the network on which you want the Vm's to
				// connect
				// to.
				else {

					NetworkConnectionSectionType networkConnectionSectionType = new NetworkConnectionSectionType();
					networkConnectionSectionType.setInfo(networkInfo);

					NetworkConnectionType networkConnectionType = new NetworkConnectionType();
					networkConnectionType.setNetwork(networkConfigOfVdc.getNetworkConfigName());
					networkConnectionType.setIpAddressAllocationMode(IpAddressAllocationModeType.DHCP.value());
					networkConnectionSectionType.getNetworkConnection().add(networkConnectionType);

					InstantiationParamsType vmInstantiationParamsType = new InstantiationParamsType();
					List<JAXBElement<? extends SectionType>> vmSections = vmInstantiationParamsType.getSection();
					vmSections.add(new ObjectFactory().createNetworkConnectionSection(networkConnectionSectionType));
					vappTemplateItem.setInstantiationParams(vmInstantiationParamsType);
				}

				items.add(vappTemplateItem);
			}
		}
		try {
			Vapp vapp = vdc.composeVapp(composeVAppParamsType);
			for (Task task : vapp.getTasks()) {
				task.waitForTask(0);
			}
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	public void createVapp(String vappName, VappDeployConfig deployConfig) {

		NetworkConfigurationType networkConfiguration = new NetworkConfigurationType();

		networkConfiguration.setParentNetwork(vdc.getAvailableNetworkRefs().iterator().next());
		networkConfiguration.setFenceMode(FenceModeValuesType.BRIDGED.value());

		VAppNetworkConfigurationType vAppNetworkConfigurationType = new VAppNetworkConfigurationType();
		vAppNetworkConfigurationType.setConfiguration(networkConfiguration);
		vAppNetworkConfigurationType.setNetworkName(vdc.getAvailableNetworkRefs().iterator().next().getName());

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
		List<SourcedCompositionItemParamType> items = composeVAppParamsType.getSourcedItem();

		for (VappTemplate temp : deployConfig.getConfig().keySet()) {
			VappTemplate template = temp.getChildren().get(0);
			Integer num = deployConfig.getConfig().get(temp);
			for (int i = 0; i < num.intValue(); i++) {
				SourcedCompositionItemParamType vappTemplateItem = new SourcedCompositionItemParamType();
				ReferenceType vappTemplateVMRef = new ReferenceType();
				vappTemplateVMRef.setHref(template.getReference().getHref());
				vappTemplateVMRef.setName(i + "-" + template.getReference().getName());
				vappTemplateItem.setSource(vappTemplateVMRef);

				// When a vApp includes Vm elements that connect to networks
				// with
				// different names, you can use a NetworkAssignment element to
				// assign the network connection for each Vm to a specific
				// network
				// in the parent vApp
				if (template.getNetworkConnectionSection().getNetworkConnection().size() > 0) {
					for (NetworkConnectionType networkConnection : template.getNetworkConnectionSection()
							.getNetworkConnection()) {
						if (networkConnection.getNetworkConnectionIndex() == template.getNetworkConnectionSection()
								.getPrimaryNetworkConnectionIndex()) {
							NetworkAssignmentType networkAssignment = new NetworkAssignmentType();
							networkAssignment.setInnerNetwork(networkConnection.getNetwork());
							networkAssignment.setContainerNetwork(vdc.getAvailableNetworkRefs().iterator().next().getName());
							List<NetworkAssignmentType> networkAssignments = vappTemplateItem.getNetworkAssignment();
							networkAssignments.add(networkAssignment);
						}
					}
				}
				// If the vApp's Vm elements does not contain any network
				// connections. The network connection settings can be edited
				// and
				// updated with the network on which you want the Vm's to
				// connect
				// to.
				else {

					NetworkConnectionSectionType networkConnectionSectionType = new NetworkConnectionSectionType();
					networkConnectionSectionType.setInfo(networkInfo);

					NetworkConnectionType networkConnectionType = new NetworkConnectionType();
					// networkConnectionType.setNetwork(networkConfigOfVdc.getNetworkConfigName());
					networkConnectionType.setIpAddressAllocationMode(IpAddressAllocationModeType.DHCP.value());
					networkConnectionSectionType.getNetworkConnection().add(networkConnectionType);

					InstantiationParamsType vmInstantiationParamsType = new InstantiationParamsType();
					List<JAXBElement<? extends SectionType>> vmSections = vmInstantiationParamsType.getSection();
					vmSections.add(new ObjectFactory().createNetworkConnectionSection(networkConnectionSectionType));
					vappTemplateItem.setInstantiationParams(vmInstantiationParamsType);
				}

				items.add(vappTemplateItem);
			}
		}
		try {
			Vapp vapp = vdc.composeVapp(composeVAppParamsType);
			for (Task task : vapp.getTasks()) {
				task.waitForTask(0);;
			}
		} catch (VCloudException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} 
	}

	public List<NetworkConfigOfVdc> listNetworkConfigOfVdcs() {
		@SuppressWarnings("deprecation")
		HashMap<String, ReferenceType> availableNetworkRefs = vdc.getAvailableNetworkRefsByName();
		for (String networkName : availableNetworkRefs.keySet()) {
			try {
				NetworkConfigOfVdc networkConfigOfVdc = new NetworkConfigOfVdc(networkName, vcloudClient,
						OrgVdcNetwork.getOrgVdcNetworkByReference(vcloudClient, availableNetworkRefs.get(networkName)));
				networkConfigOfVdcs.add(networkConfigOfVdc);
			} catch (VCloudException e) {
				e.printStackTrace();
			}

		}
		return networkConfigOfVdcs;
	}

	public NetworkConfigOfVdc getNetworkConfigOfVdc(String networkName) {
		for (NetworkConfigOfVdc networkConfigOfVdc : listNetworkConfigOfVdcs()) {
			if (networkName.equals(networkConfigOfVdc.getNetworkConfigName())) {
				return networkConfigOfVdc;
			}
		}
		return null;
	}

	public String getVdcName() {
		return vdcName;
	}

	public void setVdcName(String vdcName) {
		this.vdcName = vdcName;
	}

	public VcloudClient getVcloudClient() {
		return vcloudClient;
	}

	public void setVcloudClient(VcloudClient vcloudClient) {
		this.vcloudClient = vcloudClient;
	}

	public Vdc getVdc() {
		return vdc;
	}

	public void setVdc(Vdc vdc) {
		this.vdc = vdc;
	}

}
