package cn.com.esrichina.adapter.vcloud.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.vcloud.VCloudService;

public class VCloudOrganizationTest {
	private VCloudService vcloudService;
	private IOrganization org;
	@Before
	public void setUp() throws Exception {
		vcloudService = new VCloudService(Constants.URL, Constants.USER, 
				Constants.PASSWORD, "5.5", "all");

		org = vcloudService.getOrganizationByName(Constants.ORG_NAME);
	}

	@Test
	public void testGetOrgName() {
		try {
			assertEquals(Constants.ORG_NAME, org.getName());
			System.out.println("Org id is:" + org.getId());
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetDatacenters() {
		try {
			IDatacenter[] dcs = org.getDatacenters();
			for(IDatacenter dc : dcs) {
				System.out.println(dc.getName());
				assertEquals(Constants.ORG_VDC_NAME, dc.getName());
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDatacenterByName() {
		try {
			IDatacenter dc = org.getDatacenterByName(Constants.ORG_VDC_NAME);
			System.out.println("vdc name is:" + dc.getName());
			assertEquals(Constants.ORG_VDC_NAME, dc.getName());
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	
}
