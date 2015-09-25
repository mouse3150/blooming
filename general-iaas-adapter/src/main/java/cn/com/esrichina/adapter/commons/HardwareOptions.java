package cn.com.esrichina.adapter.commons;

public class HardwareOptions {
	private String id;
	private String name;
	private int cpuNum;
	private int coreNumPerCpu;
	private long memory;
	private long disk;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCpuNum() {
		return cpuNum;
	}

	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}

	public int getCoreNumPerCpu() {
		return coreNumPerCpu;
	}

	public void setCoreNumPerCpu(int coreNumPerCpu) {
		this.coreNumPerCpu = coreNumPerCpu;
	}

	public long getMemory() {
		return memory;
	}

	public void setMemory(long memory) {
		this.memory = memory;
	}

	public long getDisk() {
		return disk;
	}

	public void setDisk(long disk) {
		this.disk = disk;
	}

}
