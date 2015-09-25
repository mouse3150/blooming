package cn.com.esrichina.vcloud.network;

import java.util.ArrayList;
import java.util.List;

import cn.com.esrichina.vcloud.VdcEntity;

import com.vmware.vcloud.api.rest.schema.AllocatedIpAddressType;
import com.vmware.vcloud.api.rest.schema.IpRangeType;
import com.vmware.vcloud.api.rest.schema.IpRangesType;
import com.vmware.vcloud.api.rest.schema.IpScopeType;
import com.vmware.vcloud.api.rest.schema.IpScopesType;
import com.vmware.vcloud.api.rest.schema.NetworkConfigurationType;
import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.sdk.AllocatedIpAddress;
import com.vmware.vcloud.sdk.OrgVdcNetwork;
import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.Vdc;

public class NetworkConfigOfVdc {
	private String networkConfigName;
	private String fenceMode;
	private OrgVdcNetwork orgVdcNetwork;
	private VcloudClient vcloudClient;
	private List<SubNetworkConfig> subNetworkConfigs = new ArrayList<SubNetworkConfig>();;

	public NetworkConfigOfVdc() {

	}

	public NetworkConfigOfVdc(String networkConfigName, VcloudClient vcloudClient, OrgVdcNetwork orgVdcNetwork) {
		this.networkConfigName = networkConfigName;
		this.vcloudClient = vcloudClient;
		this.orgVdcNetwork = orgVdcNetwork;
	}

	public VdcEntity getVdc() {
		VdcEntity vdce = null;
		try {
			ReferenceType vdcRef = orgVdcNetwork.getVdcReference();
			Vdc vdc = Vdc.getVdcByReference(vcloudClient, vdcRef);
			vdce = new VdcEntity(vdc.getResource().getName(), vcloudClient, vdc);
		} catch (VCloudException e) {
			e.printStackTrace();
		}

		return vdce;
	}

	public String getNetworkConfigName() {
		return networkConfigName;
	}

	public String getStatus() {
		if (orgVdcNetwork.getResource().getStatus() == 1) {
			return "normal";
		} else {
			return "abnormal";
		}
	}

	public List<String> getAllocatedIpAddresses() {
		List<String> availableIpAddresses = new ArrayList<String>();
		try {
			List<AllocatedIpAddress> allocatedIpAddresses = orgVdcNetwork.getAllocatedAddresses();
			for (AllocatedIpAddress aia : allocatedIpAddresses) {
				AllocatedIpAddressType aiatype = aia.getResource();
				if (aiatype != null) {
					String ipaddress = aiatype.getIpAddress();
					//System.out.println("allocated ipaddress:" + ipaddress);
					availableIpAddresses.add(ipaddress);
				}
			}

		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return availableIpAddresses;
	}

	

	public List<String> getAllIpAddresses() {
		List<String> allIpAddresses = new ArrayList<String>();
		NetworkConfigurationType networkconfigurationtype = orgVdcNetwork.getConfiguration();

		IpScopesType ipScopesType = networkconfigurationtype.getIpScopes();
		List<IpScopeType> ipScopeTypes = ipScopesType.getIpScope();

		if (ipScopeTypes != null) {
			for (IpScopeType ipScopeType : ipScopeTypes) {

				boolean isEnabled = ipScopeType.isIsEnabled();
				if (isEnabled) {
					//boolean isinherited = ipScopeType.isIsInherited();

					IpRangesType ipRangesType = ipScopeType.getIpRanges();
					List<IpRangeType> ipRangeTypes = ipRangesType.getIpRange();
					for (IpRangeType iprangetype : ipRangeTypes) {
						String startaddress = iprangetype.getStartAddress();
						String endaddress = iprangetype.getEndAddress();

						if (startaddress != null && endaddress != null) {
							String fromSub = startaddress.substring(0, startaddress.lastIndexOf(".") + 1);

							String fromEnd = startaddress.substring(startaddress.lastIndexOf(".") + 1,
									startaddress.length());

							String toEnd = endaddress.substring(endaddress.lastIndexOf(".") + 1, endaddress.length());

							int ifrom = Integer.parseInt(fromEnd);
							int ito = Integer.parseInt(toEnd);

							for (int i = ifrom; i < ito + 1; i++) {
								String ip = fromSub + i;
								allIpAddresses.add(ip);
							}

						}

					}
				}

			}

		}
		return allIpAddresses;
	}
	
	public OrgVdcNetwork getOrgVdcNetwork() {
		return orgVdcNetwork;
	}

	public List<SubNetworkConfig> subNetworkConfigs() {
		NetworkConfigurationType networkconfigurationtype = orgVdcNetwork.getConfiguration();

		IpScopesType ipScopesType = networkconfigurationtype.getIpScopes();
		List<IpScopeType> ipScopeTypes = ipScopesType.getIpScope();

		if (ipScopeTypes != null) {
			for (IpScopeType ipScopeType : ipScopeTypes) {
				SubNetworkConfig subNetworkConfig = new SubNetworkConfig();
				boolean isEnabled = ipScopeType.isIsEnabled();
				if (isEnabled) {
					//boolean isinherited = ipScopeType.isIsInherited();
					
					subNetworkConfig.setDns1(ipScopeType.getDns1());
					subNetworkConfig.setDns2(ipScopeType.getDns2());
					subNetworkConfig.setDnsSuffix(ipScopeType.getDnsSuffix());
					subNetworkConfig.setGateway(ipScopeType.getGateway());
					
					List<IpRange> ipRanges = new ArrayList<IpRange>();
					for(IpRangeType ipRangeType : ipScopeType.getIpRanges().getIpRange()) {
						IpRange ipRange = new IpRange();
						ipRange.setStartIpAddress(ipRangeType.getStartAddress());
						ipRange.setEndIpAddress(ipRangeType.getEndAddress());
						ipRanges.add(ipRange);
					}
					
					subNetworkConfig.setIpRanges(ipRanges);
					subNetworkConfigs.add(subNetworkConfig);				
				}
			}
		}
		return subNetworkConfigs;
	}

	public String getFenceMode() {
		if (fenceMode != null) {
			return fenceMode;
		}

		return orgVdcNetwork.getConfiguration().getFenceMode();
	}

}
