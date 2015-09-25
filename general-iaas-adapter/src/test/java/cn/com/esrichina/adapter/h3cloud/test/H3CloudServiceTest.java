package cn.com.esrichina.adapter.h3cloud.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.VmFlavor;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.H3CloudService;

public class H3CloudServiceTest {

	private  H3CloudMethod method;
	private H3CloudService service;
	
	@Before
	public void setUp() throws Exception {
		method = new H3CloudMethod(H3Constants.endpoint, H3Constants.username, H3Constants.password);
		service = new H3CloudService(method);
	}

	@Test
	public void testGetOrgs() {
		try {
			IOrganization[] orgs = service.getOrganizations();
			if(orgs != null && orgs.length > 0) {
				for(int i = 0; i < orgs.length; i++) {
					System.out.println(orgs[i].getName());
				}
			}
			
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetOrgByName() {
		try {
			IOrganization org = service.getOrganizationByName(H3Constants.org_name);
			System.out.println(org.getName());
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetFlavorById() {
		try {
			VmFlavor flavor = service.getVmFlavor(H3Constants.flavor_id);
			System.out.println(flavor.getName());
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetFlavors() {
		try {
			List<VmFlavor> flavors = service.getVmFlavors();
			for(VmFlavor v : flavors) {
				System.out.println(v.getName());
			}
			
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
