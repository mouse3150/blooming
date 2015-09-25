package cn.com.esrichina.adapter.h3cloud.test;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.h3cloud.H3CloudException;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;

public class H3CloudMethodTest {
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDelete() {
		H3CloudMethod method = new H3CloudMethod(H3Constants.endpoint, H3Constants.username, H3Constants.password );
		try {
			String result = method.delete("server", H3Constants.vm_id);
			System.out.println(result);
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPost() {
		H3CloudMethod method = new H3CloudMethod(H3Constants.endpoint, H3Constants.username, H3Constants.password);
		String payload = "<?xml version=\"1.0\"?> <flavor> <vcpu>4</vcpu><memory>4</memory><disk>120</disk></flavor>";
		try {
			String result = method.post("flavor", null, payload, null, "application/json");
			System.out.println(result);
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDeleteVm() {
		H3CloudMethod method = new H3CloudMethod(H3Constants.endpoint, H3Constants.username, H3Constants.password);
		try {
			String result = method.delete("server", "52434409-0292-4c71-bec7-9db04c9fa843");
			System.out.println(result);
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPostCreateVM() {
		H3CloudMethod method = new H3CloudMethod(H3Constants.endpoint, H3Constants.username, H3Constants.password);
		String payload = "<server> <name>test2</name><description>create2.</description><imageUuid>7ae01caa-583a-40e2-b483-c992477e0454</imageUuid>" + 
		"<networkUuid>4d4464d8-910d-4845-812f-24f4e8f33d11</networkUuid><flavorUuid>edb977c2-3231-49fb-81ec-98268807c0fa</flavorUuid></server>";
		try {
			String result = method.post("server", null, payload, null, "application/json");
			System.out.println(result);
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
	}


}
