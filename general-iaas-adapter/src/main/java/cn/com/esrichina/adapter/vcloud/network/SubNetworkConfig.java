package cn.com.esrichina.adapter.vcloud.network;

import java.util.List;

public class SubNetworkConfig {
	private String gateway;
	private String mask;
	private String dns1;
	private String dns2;
	private String dnsSuffix;
	private List<IpRange> ipRanges;

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
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

	public List<IpRange> getIpRanges() {
		return ipRanges;
	}

	public void setIpRanges(List<IpRange> ipRanges) {
		this.ipRanges = ipRanges;
	}

}