package cn.com.esrichina.adapter.jcs.openstack.test;

import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.Constants;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.commons.VmFlavor;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.jcs.openstack.JcsOpenstackServiceBuilder;

public class JcsOpenstackServiceBuilderTest {
	private Properties pros;
	@Before
	public void setUp() throws Exception {
		pros = new Properties();
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_ENDPOINT, JcsOpenstackContants.ENDPOINT);
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_REGION, JcsOpenstackContants.REGION_ID);
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_ORG_NAME, JcsOpenstackContants.TENANT_NAME);
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_IDENTIFY, JcsOpenstackContants.ACCESS_IDENTIFY);
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_CREDENTIAL, JcsOpenstackContants.ACCESS_PASSWORD);
	}

	@Test
	public void testGetOrgs() {
		JcsOpenstackServiceBuilder builder = new JcsOpenstackServiceBuilder(pros);
		
		try {
			IaaSService service = builder.createIaaSService();
			
			IOrganization[] orgs = service.getOrganizations();
			
			for (int i = 0; i < orgs.length; i++) {
				System.out.println(orgs[i].getName());
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetOrgByName() {
		JcsOpenstackServiceBuilder builder = new JcsOpenstackServiceBuilder(pros);
		
		try {
			IaaSService service = builder.createIaaSService();
			
			IOrganization org = service.getOrganizationByName(JcsOpenstackContants.TENANT_NAME);
			System.out.println("ORG NAME:" + org.getName() + ", ID:" + org.getId());
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}

	
	@Test
	public void testGetCurrentOrg() {
		JcsOpenstackServiceBuilder builder = new JcsOpenstackServiceBuilder(pros);
		
		try {
			IaaSService service = builder.createIaaSService();
			
			IOrganization org = service.getCurrentOrganization();
			System.out.println("Current ORG NAME:" + org.getName() + ", ID:" + org.getId());
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetInfo() {
		JcsOpenstackServiceBuilder builder = new JcsOpenstackServiceBuilder(pros);
		
		try {
			IaaSService service = builder.createIaaSService();
			
			System.out.println("Provider name:" + service.getProviderName());
			System.out.println("Cloud name:" + service.getCloudName());
			System.out.println("Cloud version:" + service.getCloudVersion());
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testListVmFlavors() {
		JcsOpenstackServiceBuilder builder = new JcsOpenstackServiceBuilder(pros);
		
		try {
			IaaSService service = builder.createIaaSService();
			
			List<VmFlavor> flavors = service.getVmFlavors();
			
			for(VmFlavor f : flavors) {
				System.out.println(f);
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testListVmFlavorById() {
		JcsOpenstackServiceBuilder builder = new JcsOpenstackServiceBuilder(pros);
		
		try {
			IaaSService service = builder.createIaaSService();
			
			VmFlavor flavor = service.getVmFlavor("2");
			
			System.out.println(flavor);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
}
