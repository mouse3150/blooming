package cn.com.esrichina.adapter.feature;

public class VirtualMachineFeature {
	// FIXME add custom configuration for change some config in the template
    private String name;
    private String templateName;
    private int cpuNums;
    private int memory; //MB
    private int disk; //GB
    
    private String vmType;
    private String inVlanId;
    
    private String keypair;
    private String[] protectedByFirewalls;
    
    private String hostName;
    private String standerName;
    private String datastoreName;
    
    private String hostHostName;
    private String hostUserName;
    private String hostPassword;
    private String vmIp;
    private DatacenterFeature datacenterFeature;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * Count of CPU in VM
     */
    public int getCpuNums() {
        return cpuNums;
    }

    /**
     * @param cpuCount count of CPU in VM
     */
    public void setCpuNums(int cpuNums) {
        this.cpuNums = cpuNums;
    }

    /**
     * Memory size(MB) in VM
     */
    public int getMemory() {
        return memory;
    }

    /**
     * @param memory Memory size(MB) in VM
     */
    public void setMemory(int memory) {
        this.memory = memory;
    }

    /**
     * Disk size(GB) in VM
     */
    public int getDisk() {
        return disk;
    }

    /**
     * @param disk Disk size(GB) in VM
     */
    public void setDisk(int disk) {
        this.disk = disk;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getStanderName() {
        return standerName;
    }

    public void setStanderName(String standerName) {
        this.standerName = standerName;
    }

    public String getDatastoreName() {
        return datastoreName;
    }

    public void setDatastoreName(String datastoreName) {
        this.datastoreName = datastoreName;
    }

	public String getVmType() {
		return vmType;
	}

	public void setVmType(String vmType) {
		this.vmType = vmType;
	}

	public String getInVlanId() {
		return inVlanId;
	}

	public void setInVlanId(String inVlanId) {
		this.inVlanId = inVlanId;
	}

	public String getKeypair() {
		return keypair;
	}

	public void setKeypair(String keypair) {
		this.keypair = keypair;
	}

	public String[] getProtectedByFirewalls() {
		return protectedByFirewalls;
	}

	public void setProtectedByFirewalls(String[] protectedByFirewalls) {
		this.protectedByFirewalls = protectedByFirewalls;
	}



	public String getHostHostName() {
		return hostHostName;
	}

	public void setHostHostName(String hostHostName) {
		this.hostHostName = hostHostName;
	}

	public String getHostUserName() {
		return hostUserName;
	}

	public void setHostUserName(String hostUserName) {
		this.hostUserName = hostUserName;
	}

	public String getHostPassword() {
		return hostPassword;
	}

	public void setHostPassword(String hostPassword) {
		this.hostPassword = hostPassword;
	}

	public String getVmIp() {
		return vmIp;
	}

	public void setVmIp(String vmIp) {
		this.vmIp = vmIp;
	}


	public DatacenterFeature getDatacenterFeature() {
		return datacenterFeature;
	}

	public void setDatacenterFeature(DatacenterFeature datacenterFeature) {
		this.datacenterFeature = datacenterFeature;
	}
}
