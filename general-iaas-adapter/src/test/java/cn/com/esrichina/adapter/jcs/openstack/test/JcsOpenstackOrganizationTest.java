package cn.com.esrichina.adapter.jcs.openstack.test;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.Constants;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.jcs.openstack.JcsOpenstackServiceBuilder;

public class JcsOpenstackOrganizationTest {

	private Properties pros;
	private IaaSService service;
	private IOrganization org;
	@Before
	public void setUp() throws Exception {
		pros = new Properties();
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_ENDPOINT, JcsOpenstackContants.ENDPOINT);
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_REGION, JcsOpenstackContants.REGION_ID);
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_ORG_NAME, JcsOpenstackContants.TENANT_NAME);
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_IDENTIFY, JcsOpenstackContants.ACCESS_IDENTIFY);
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_CREDENTIAL, JcsOpenstackContants.ACCESS_PASSWORD);
		
		JcsOpenstackServiceBuilder builder = new JcsOpenstackServiceBuilder(pros);
		
		service = builder.createIaaSService();
		org = service.getCurrentOrganization();
	}

	@Test
	public void testGetInfo() {
		try {
			System.out.println("ORG NAME:" + org.getName() + ", ORG ID:" + org.getId());
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetDcs() {
		try {
			IDatacenter[] dcs = org.getDatacenters();
			
			for (int i = 0; i < dcs.length; i++) {
				System.out.println(dcs[i].getName());
			}
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetDc() {
		try {
			IDatacenter dc = org.getDatacenterByName("admin-DC");
			
			System.out.println(dc.getName());
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
