package cn.com.esrichina.adapter.commons;

public class HostConfiguration {
	private ComputeResource computeResource;

    public ComputeResource getComputeResource() {
        return computeResource;
    }

    public void setComputeResource(ComputeResource computeResource) {
        this.computeResource = computeResource;
    }

    /**
     * Count of CPU in VM
     */
    public int getCpuNum() {
        if (computeResource != null) {
            return computeResource.getCpuNum();
        }
        return 0;
    }

    /**
     * @param cpuCount
     * count of CPU in VM
     */
    public void setCpuNum(int cpuNums) {
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
    

}
