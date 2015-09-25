package cn.com.esrichina.adapter.commons;

public class ImageConfiguration {
	private ComputeResource computeResource;
	private OsInfo osInfo;
	private int diskSize;

	public ComputeResource getComputeResource() {
		return computeResource;
	}

	public void setComputeResource(ComputeResource computeResource) {
		this.computeResource = computeResource;
	}


	public int getCpuNums() {
		if (computeResource != null) {
			return computeResource.getCpuNum();
		}
		return 0;
	}


	public void setCpuNums(int cpuNums) {
		if (computeResource == null) {
			this.computeResource = new ComputeResource();
		}
		computeResource.setCpuNum(cpuNums);
	}

	/**
	 * Memory size(MB) in VM
	 */
	public int getMemory() {
		if (computeResource != null) {
			return computeResource.getMemory();
		}
		return 0;
	}

	/**
	 * @param memory
	 * Memory size(MB) in VM
	 */
	public void setMemory(int memory) {
		if (computeResource == null) {
			this.computeResource = new ComputeResource();
		}
		computeResource.setMemory(memory);
	}

	public OsInfo getOsInfo() {
		return osInfo;
	}

	public void setOsInfo(OsInfo osInfo) {
		this.osInfo = osInfo;
	}

	/**
	 * Disk size(GB)
	 */
	public int getDiskSize() {
		return diskSize;
	}

	/**
	 * Disk size(GB)
	 */
	public void setDiskSize(int size) {
		this.diskSize = size;
	}
}
