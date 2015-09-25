package cn.com.esrichina.vcloud.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.vcloud.OrgEntity;
import cn.com.esrichina.vcloud.VcloudService;

public class VcloudServiceTest {
	
	private static VcloudService vcloudService = null;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConnection() {
		
		try {
			 vcloudService = new VcloudService(Constants.URL, Constants.USER, Constants.PASSWORD);
		} catch (Exception e) {
			fail("连接vCloud失败!");
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testListOrg() {
		OrgEntity org = vcloudService.getOrgByName(Constants.ORG_NAME);
		assertEquals(Constants.ORG_NAME, org.getOrgName());
	}
	
	@Test
	public void testListVdcs() {
		List<OrgEntity> orgs = vcloudService.listOrgs();
		if(orgs.isEmpty()) {
			fail(Constants.USER  + " have no organizations.");
		}
		
	}

}
