package cn.com.esrichina.adapter.commons;

public class VirtualMachineRuntimeInfo {
	private String privateIp;
    
	private String hostName;
    
	private OsInfo osInfo;
    
	private VmStatus status;
    
    public String getPrivateIp() {
        return privateIp;
    }

    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public OsInfo getOsInfo() {
        return osInfo;
    }

    public void setOsInfo(OsInfo osInfo) {
        this.osInfo = osInfo;
    }

	public VmStatus getStatus() {
		return status;
	}

	public void setStatus(VmStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "VirtualMachineRuntimeInfo [privateIp=" + privateIp + ", hostName=" + hostName + ", osInfo=" + osInfo
				+ ", status=" + status + "]";
	}
	
	
}
