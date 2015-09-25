package cn.com.esrichina.adapter.h3cloud;

public class H3CloudFlavor {

	private String uuid;
	private String name;
	/**
	 * vcpu 虚拟CPU，单位：个
	 */
	private int vcpu;
	/**
	 * memory 内存大小 ，单位：G
	 */
	private int memory;
	/**
	 * disk 磁盘大小，使用时磁盘不能小于模板定义大小，单位:G
	 */
	private int disk;

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

	public int getVcpu() {
		return vcpu;
	}

	public void setVcpu(int vcpu) {
		this.vcpu = vcpu;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public int getDisk() {
		return disk;
	}

	public void setDisk(int disk) {
		this.disk = disk;
	}
	
	public String toString() {
		return "[id:'" + this.uuid +"', name:'" + this.name 
				+ "', vcpu:" + this.vcpu + ", memory:" + this.memory
				+ ", disk:" + this.disk + "]";
	}

}
