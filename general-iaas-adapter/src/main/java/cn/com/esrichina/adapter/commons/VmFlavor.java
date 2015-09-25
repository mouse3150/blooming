package cn.com.esrichina.adapter.commons;

public class VmFlavor {
	private String id;
	private String name;

	private int vcpu;

	/**
	 * 单位:G
	 */
	private int memory;

	/**
	 * 单位：G
	 */
	private int disk;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getVcpu() {
		return vcpu;
	}

	public int getMemory() {
		return memory;
	}

	public int getDisk() {
		return disk;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVcpu(int vcpu) {
		this.vcpu = vcpu;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public void setDisk(int disk) {
		this.disk = disk;
	}

	@Override
	public String toString() {
		return "VmFlavor [id=" + id + ", name=" + name + ", vcpu=" + vcpu 
					+ ", memory=" + memory + ", disk=" + disk + "]";
	}
	
	
}
