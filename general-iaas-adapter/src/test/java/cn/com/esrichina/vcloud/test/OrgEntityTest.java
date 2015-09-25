package cn.com.esrichina.vcloud.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.vcloud.VcloudService;

public class OrgEntityTest {
	private VcloudService vcloudService;
	@Before
	public void setUp() throws Exception {
		vcloudService = new VcloudService(Constants.URL, Constants.USER, Constants.PASSWORD);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
