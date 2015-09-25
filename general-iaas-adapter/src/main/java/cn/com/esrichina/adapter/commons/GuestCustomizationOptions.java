package cn.com.esrichina.adapter.commons;

public class GuestCustomizationOptions {
	private Platform platform;
	private String computeName;
	private String adminPassword;
	private String customizationScript;
	
	// forWin
	private String sid;
	private String domainName;
	private String domainUserName;
	private String domainUserPassword;
	private String dnsSuffix;
	
	
	public GuestCustomizationOptions(String computeName, String adminPassword, String customizationScript, String sid,
			String domainName, String domainUserName, String domainUserPassword) {
		super();
		this.computeName = computeName;
		this.adminPassword = adminPassword;
		this.customizationScript = customizationScript;
		this.sid = sid;
		this.domainName = domainName;
		this.domainUserName = domainUserName;
		this.domainUserPassword = domainUserPassword;
	}
	
	public GuestCustomizationOptions() {
		
	}
	public String getComputeName() {
		return computeName;
	}
	public void setComputeName(String computeName) {
		this.computeName = computeName;
	}
	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	public String getCustomizationScript() {
		return customizationScript;
	}
	public void setCustomizationScript(String customizationScript) {
		this.customizationScript = customizationScript;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getDomainUserName() {
		return domainUserName;
	}
	public void setDomainUserName(String domainUserName) {
		this.domainUserName = domainUserName;
	}
	public String getDomainUserPassword() {
		return domainUserPassword;
	}
	public void setDomainUserPassword(String domainUserPassword) {
		this.domainUserPassword = domainUserPassword;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getDnsSuffix() {
		return dnsSuffix;
	}

	public void setDnsSuffix(String dnsSuffix) {
		this.dnsSuffix = dnsSuffix;
	}
	
}
