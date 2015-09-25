package cn.com.esrichina.adapter.h3cloud.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.h3cloud.H3CloudException;
import cn.com.esrichina.adapter.h3cloud.H3CloudImage;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.service.H3CImageService;

public class ImageServiceTest {

	private  H3CloudMethod method;
	private H3CImageService imageService;
	
	@Before
	public void setUp() throws Exception {
		method = new H3CloudMethod(H3Constants.endpoint, H3Constants.username, H3Constants.password);
		imageService = new H3CImageService(method);
	}

	@Test
	public void testGetImages() {
		try {
			List<H3CloudImage> images = imageService.getH3CImages();
			
			for(H3CloudImage i : images) {
				System.out.println(i);
			}
		} catch (H3CloudException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetImagesByCondition() {
		try {
			List<H3CloudImage> images = imageService.getH3CImages(0, 10);
			
			for(H3CloudImage i : images) {
				System.out.println(i);
			}
		} catch (H3CloudException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetImagesById() {
		try {
			H3CloudImage images = imageService.getH3CImageById(H3Constants.image_id);
			
			System.out.println(images);
			
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
	}

}
