package cn.com.esrichina.adapter.h3cloud;

public class H3CloudServer {
	private String name;
	private String description;
	private String uuid;
	private String imageUuid;
	private String networkUuid;
	private String flavorUuid;
	private String owner;
	/**
	 * 根据H3Cloud接口文档描述：
	 * 主机状态：
	 * active 正常
	 * error 异常
	 */
	private String state;
	
	/**
	 * 电源状态（整型）：
	 * 0  未知
	 * 1 正常
	 */
	private int powerState;
	private String privateIp;
	private String createTime;
	private int organizationId;
	private String password;
	private String vncUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getImageUuid() {
		return imageUuid;
	}

	public void setImageUuid(String imageUuid) {
		this.imageUuid = imageUuid;
	}

	public String getNetworkUuid() {
		return networkUuid;
	}

	public void setNetworkUuid(String networkUuid) {
		this.networkUuid = networkUuid;
	}

	public String getFlavorUuid() {
		return flavorUuid;
	}

	public void setFlavorUuid(String flavorUuid) {
		this.flavorUuid = flavorUuid;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getPowerState() {
		return powerState;
	}

	public void setPowerState(int powerState) {
		this.powerState = powerState;
	}

	public String getPrivateIp() {
		return privateIp;
	}

	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVncUrl() {
		return vncUrl;
	}

	public void setVncUrl(String vncUrl) {
		this.vncUrl = vncUrl;
	}

}
