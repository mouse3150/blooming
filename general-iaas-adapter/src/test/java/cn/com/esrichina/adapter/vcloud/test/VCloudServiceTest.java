package cn.com.esrichina.adapter.vcloud.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.vcloud.VCloudService;

public class VCloudServiceTest {
	
	private VCloudService vcloudService;

	@Before
	public void setUp() throws Exception {
		vcloudService = new VCloudService(Constants.URL, Constants.USER, Constants.PASSWORD, "5.5", "all");
	}

	@Test
	public void initDefaultService() {
		try {
			vcloudService = new VCloudService();
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void initServiceByParams() {
		//VCloudService(String url, String user, String password, String version, String logLevel)
		try {
			vcloudService = new VCloudService(Constants.URL, Constants.USER, Constants.PASSWORD, "5.5", "all");
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetOrganizations() {
		try {
			IOrganization[] orgs = vcloudService.getOrganizations();
			for(IOrganization org : orgs) {
				org.getName();
				System.out.println(org.getName());
				assertEquals(Constants.ORG_NAME, org.getName());
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetOrganizationByName() {
		try {
			IOrganization org = vcloudService.getOrganizationByName(Constants.ORG_NAME);
			System.out.println(org.getName());
			assertEquals(Constants.ORG_NAME, org.getName());
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getFlavors() {
		try {
			vcloudService.getVmFlavors();
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
