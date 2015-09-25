package cn.com.esrichina.adapter.h3cloud.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.h3cloud.H3CloudException;
import cn.com.esrichina.adapter.h3cloud.H3CloudFlavor;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.service.H3CFlavorService;

public class FlavorServiceTest {

	private  H3CloudMethod method;
	private H3CFlavorService flavorService;
	
	@Before
	public void setUp() throws Exception {
		method = new H3CloudMethod(H3Constants.endpoint, H3Constants.username, H3Constants.password);
		flavorService = new H3CFlavorService(method);
	}

	@Test
	public void testGetFlavors() {
		try {
			List<H3CloudFlavor> flavors = flavorService.getH3CFlavors();
			for(H3CloudFlavor f : flavors) {
				System.out.println(f);
			}
		} catch (H3CloudException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetFlavorsByCondition() {
		try {
			List<H3CloudFlavor> flavors = flavorService.getH3CFlavors(0, 100);
			for(H3CloudFlavor f : flavors) {
				System.out.println(f);
			}
		} catch (H3CloudException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testGetFlavorsById() {
		String uuid = H3Constants.flavor_id;
		try {
			H3CloudFlavor flavor = flavorService.getH3CFlavorById(uuid);
			System.out.println(flavor);
			
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreate() {
		try {
			H3CloudFlavor flavor = flavorService.createFlavor(2, 2, 100);
			System.out.println(flavor);
			
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
	}
}
