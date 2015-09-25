package cn.com.esrichina.vcloud;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import cn.com.esrichina.adapter.utils.FakeSSLSocketFactory;

import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.sdk.Organization;
import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.constants.Version;

public class VcloudService {
	private VcloudClient vcloudClient;
	private static final String URL = "https://192.168.190.83";
	
	private static final String USER = "admin@online";
	
	private static final String PASSWORD = "esri@123";
	
	public VcloudService() {
		VcloudClient.setLogLevel(Level.OFF);
		VcloudClient vcloudClient = new VcloudClient(URL, Version.V5_5);

		try {
			vcloudClient.registerScheme("https", 443, FakeSSLSocketFactory
					.getInstance());
			
			vcloudClient.login(USER, PASSWORD);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (VCloudException e) {
			e.printStackTrace();
		}

		this.vcloudClient = vcloudClient;
	}
	
	public VcloudService(String url, String user, String password) {
		VcloudClient.setLogLevel(Level.OFF);
		VcloudClient vcloudClient = new VcloudClient(url, Version.V5_5);

		try {
			vcloudClient.registerScheme("https", 443, FakeSSLSocketFactory
					.getInstance());
			
			vcloudClient.login(user, password);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (VCloudException e) {
			e.printStackTrace();
		}

		this.vcloudClient = vcloudClient;
	}
	
	public VcloudService(VcloudClient vcloudClient) {
		this.vcloudClient = vcloudClient;
	}
	
	
	public List<OrgEntity> listOrgs() {
		List<OrgEntity> orgs = new ArrayList<OrgEntity>();
		HashMap<String, ReferenceType> orgsList;
		try {
			orgsList = vcloudClient
					.getOrgRefsByName();
			for (ReferenceType orgRef : orgsList.values()) {
				Organization org = Organization.getOrganizationByReference(vcloudClient, orgRef);
				OrgEntity oe = new OrgEntity(vcloudClient, org);
				orgs.add(oe);
			}
			
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		
		return orgs;
	}
	
	public OrgEntity getOrgByName(String name) {
		if(name == null) {
			throw new RuntimeException("Organization can not be null.");
		}
		for(OrgEntity org : listOrgs()) {
			if(name.equals(org.getOrgName())) {
				return org;
			}
		}
		
		return null;
	}
	
}
