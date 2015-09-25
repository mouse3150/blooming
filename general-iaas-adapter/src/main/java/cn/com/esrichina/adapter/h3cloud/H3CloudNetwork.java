package cn.com.esrichina.adapter.h3cloud;

public class H3CloudNetwork {
	private String uuid;
	private String name;
	/**
	 * format like: 192.168.3.0/24
	 */
	private String cidr;
	private boolean share;
	private String gatewayIP;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCidr() {
		return cidr;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	public boolean isShare() {
		return share;
	}

	public void setShare(boolean share) {
		this.share = share;
	}

	public String getGatewayIP() {
		return gatewayIP;
	}

	public void setGatewayIP(String gatewayIP) {
		this.gatewayIP = gatewayIP;
	}
	
	public String toString() {
		return "[uuid:'" + this.uuid + "', name:'" + this.name 
				+ "', cidr:'" + this.cidr + ", gatewayIP:'" + this.gatewayIP
				+ ", share:'" + this.share + "']";
	}
}
