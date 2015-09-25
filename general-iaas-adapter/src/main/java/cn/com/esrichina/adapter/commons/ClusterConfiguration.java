package cn.com.esrichina.adapter.commons;

public class ClusterConfiguration {
	private boolean pooled;
    private ComputeResource computeResource;
    public ClusterConfiguration() {
    	
    }
    
    public ClusterConfiguration(boolean pooled, ComputeResource computeResource) {
    	this.pooled = pooled;
    	this.computeResource = computeResource;
    }

    public boolean isPooled() {
        return pooled;
    }

    public void setPooled(boolean pooled) {
        this.pooled = pooled;
    }

    public ComputeResource getComputeResource() {
        return computeResource;
    }

    public void setComputeResource(ComputeResource computeResource) {
        this.computeResource = computeResource;
    }
}
