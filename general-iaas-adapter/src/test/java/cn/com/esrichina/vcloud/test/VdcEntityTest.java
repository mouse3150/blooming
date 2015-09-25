package cn.com.esrichina.vcloud.test;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.vcloud.OrgEntity;
import cn.com.esrichina.vcloud.VappDeployConfig;
import cn.com.esrichina.vcloud.VcloudService;
import cn.com.esrichina.vcloud.VdcEntity;

import com.vmware.vcloud.sdk.VappTemplate;

public class VdcEntityTest {
	public static final String TEMPLATE_NAME = "wa1022windows";
	public static final String VAPP_NAME = "for-test";
	private static VcloudService vcloudService = null;
	private static OrgEntity org = null;
	private static VdcEntity vdc = null;
	@Before
	public void setUp() throws Exception {
		vcloudService = new VcloudService(Constants.URL, Constants.USER, Constants.PASSWORD);
		org = vcloudService.getOrgByName(Constants.ORG_NAME);
		vdc = org.getVdcByName(Constants.ORG_VDC_NAME);
	}
	
	@Test
	public void testCreateVapp() {
		VappDeployConfig deployConfig = new VappDeployConfig();
		Map<VappTemplate,Integer> map = new HashMap<VappTemplate,Integer>();
		
		VappTemplate vapptemp = vdc.getVappTemplateByName(TEMPLATE_NAME);
		
		map.put(vapptemp, 1);
		
		deployConfig.setConfig(map);
		
		
		vdc.createVapp(VAPP_NAME, deployConfig);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
