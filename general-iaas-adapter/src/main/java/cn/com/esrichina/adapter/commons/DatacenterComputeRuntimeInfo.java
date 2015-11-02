package cn.com.esrichina.adapter.commons;

public class DatacenterComputeRuntimeInfo {
	/**
	 * cpu总数
	 */
	private Cpu totalCpu;
	/**
	 * 已用CPU
	 */
	private Cpu usedCpu;

	/**
	 * 内存总数
	 */
	private Memory totalMemory;
	
	/**
	 * 已用内存
	 */
	private Memory usedMemory;

	/**
	 * 数据中心资源使用是否受限
	 * 缺省配额限制
	 */
	private boolean isLimited = true;

	public Cpu getTotalCpu() {
		return totalCpu;
	}

	public void setTotalCpu(Cpu totalCpu) {
		this.totalCpu = totalCpu;
	}

	public Cpu getUsedCpu() {
		return usedCpu;
	}

	public void setUsedCpu(Cpu usedCpu) {
		this.usedCpu = usedCpu;
	}

	public Memory getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(Memory totalMemory) {
		this.totalMemory = totalMemory;
	}

	public Memory getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(Memory usedMemory) {
		this.usedMemory = usedMemory;
	}

	public boolean isLimited() {
		return isLimited;
	}

	public void setLimited(boolean isLimited) {
		this.isLimited = isLimited;
	}

	@Override
	public String toString() {
		return "DatacenterComputeRuntimeInfo [totalCpu=" + totalCpu
				+ ", usedCpu=" + usedCpu + ", totalMemory=" + totalMemory
				+ ", usedMemory=" + usedMemory + ", isLimited=" + isLimited
				+ "]";
	}

}
