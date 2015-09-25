package cn.com.esrichina.adapter.h3cloud.test;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.H3CloudService;
import cn.com.esrichina.adapter.h3cloud.domain.H3CloudOrganization;

public class H3CloudOrganizationTest {

	private H3CloudMethod method;
	private H3CloudService service;
	private H3CloudOrganization org;

	@Before
	public void setUp() throws Exception {
		method = new H3CloudMethod(H3Constants.endpoint, H3Constants.username, H3Constants.password);
		service = new H3CloudService(method);
		org = (H3CloudOrganization)service.getOrganizationByName(H3Constants.org_name);
	}

	@Test
	public void test() {
		try {
			System.out.println(org.getName());
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testGetDatacenters() {
		try {
			IDatacenter[] dcs = org.getDatacenters();
			if(dcs != null) {
				for(int i = 0; i < dcs.length; i++) {
					System.out.println(dcs[i].getName());
				}
			}
			
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDatacenterById() {
		try {
			IDatacenter dcs = org.getDatacenterByName(H3Constants.dc_name);
			
			System.out.println(dcs.getName());
			System.out.println(dcs.getId());
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
