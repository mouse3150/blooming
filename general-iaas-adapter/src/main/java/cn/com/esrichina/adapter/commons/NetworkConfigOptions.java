package cn.com.esrichina.adapter.commons;

public class NetworkConfigOptions {
	
	private String networkConfigId;
	private String networkConfigName;
	private String publicIpAddress;
	private String privateIpAddress;
	private String gateway;
	private String subnetAddress;
	private String netmask;
	private String dns1;
	private String dns2;
	private String dnsSuffix;

	public String getNetworkConfigId() {
		return networkConfigId;
	}

	public void setNetworkConfigId(String networkConfigId) {
		this.networkConfigId = networkConfigId;
	}

	public String getNetworkConfigName() {
		return networkConfigName;
	}

	public void setNetworkConfigName(String networkConfigName) {
		this.networkConfigName = networkConfigName;
	}

	public String getPublicIpAddress() {
		return publicIpAddress;
	}

	public void setPublicIpAddress(String publicIpAddress) {
		this.publicIpAddress = publicIpAddress;
	}

	public String getPrivateIpAddress() {
		return privateIpAddress;
	}

	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getSubnetAddress() {
		return subnetAddress;
	}

	public void setSubnetAddress(String subnetAddress) {
		this.subnetAddress = subnetAddress;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getDns1() {
		return dns1;
	}

	public void setDns1(String dns1) {
		this.dns1 = dns1;
	}

	public String getDns2() {
		return dns2;
	}

	public void setDns2(String dns2) {
		this.dns2 = dns2;
	}

	public String getDnsSuffix() {
		return dnsSuffix;
	}

	public void setDnsSuffix(String dnsSuffix) {
		this.dnsSuffix = dnsSuffix;
	}
}
