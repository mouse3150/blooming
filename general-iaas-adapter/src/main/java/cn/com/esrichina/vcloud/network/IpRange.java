package cn.com.esrichina.vcloud.network;

public class IpRange {
	private String startIpAddress;
	private String endIpAddress;
	public IpRange() {
		
	}
	public String getStartIpAddress() {
		return startIpAddress;
	}
	public void setStartIpAddress(String startIpAddress) {
		this.startIpAddress = startIpAddress;
	}
	public String getEndIpAddress() {
		return endIpAddress;
	}
	public void setEndIpAddress(String endIpAddress) {
		this.endIpAddress = endIpAddress;
	}
	
}
