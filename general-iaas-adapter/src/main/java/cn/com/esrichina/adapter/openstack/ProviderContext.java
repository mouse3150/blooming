/*
 * Copyright (c) 2008-2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.09.29
 */
package cn.com.esrichina.adapter.openstack;

import java.util.Properties;

public class ProviderContext {
	private String accessPrivate;
    private String accessPublic;
    private String cloudName;
    private Properties customProperties;
    private String endpoint;
    private OpenStackProvider provider;
    private String regionId;
    
    
    private String accountNumber;
    
	public String getAccessPrivate() {
		return accessPrivate;
	}
	public void setAccessPrivate(String accessPrivate) {
		this.accessPrivate = accessPrivate;
	}
	public String getAccessPublic() {
		return accessPublic;
	}
	public void setAccessPublic(String accessPublic) {
		this.accessPublic = accessPublic;
	}
	public String getCloudName() {
		return cloudName;
	}
	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}
	public Properties getCustomProperties() {
		return customProperties;
	}
	public void setCustomProperties(Properties customProperties) {
		this.customProperties = customProperties;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public OpenStackProvider getProvider() {
		return provider;
	}
	public void setProvider(OpenStackProvider provider) {
		this.provider = provider;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
}
