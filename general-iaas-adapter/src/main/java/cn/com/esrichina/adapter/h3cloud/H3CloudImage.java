package cn.com.esrichina.adapter.h3cloud;

public class H3CloudImage {
	private String uuid;
	private String name;
	private String description;
	
	/**
	 * 0 : predefined  预定义
	 * 1   ： customized  自定义 
	 */
	private int type;
	
	/**
	 * active：  正常
	 * error:  异常
	 */
	private String state;
	private String format;
	private String createTime;

	private int minDisk;
	private int minRam;
	private int coreCount;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getMinDisk() {
		return minDisk;
	}

	public void setMinDisk(int minDisk) {
		this.minDisk = minDisk;
	}

	public int getMinRam() {
		return minRam;
	}

	public void setMinRam(int minRam) {
		this.minRam = minRam;
	}

	public int getCoreCount() {
		return coreCount;
	}

	public void setCoreCount(int coreCount) {
		this.coreCount = coreCount;
	}

	public String toString() {
		return "[uuid:'" + this.uuid + "', name:'" + this.name 
				+ "', description:'" + this.description + ", state:'" + this.state + "']";
	}
}
