package cn.com.esrichina.vcloud.network.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.vcloud.OrgEntity;
import cn.com.esrichina.vcloud.VcloudService;
import cn.com.esrichina.vcloud.VdcEntity;
import cn.com.esrichina.vcloud.network.NetworkConfigOfVdc;
import cn.com.esrichina.vcloud.test.Constants;

public class NetworkConfigOfVdcTest {
	
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
	public void testGetAllocatedIpAddresses() {
		NetworkConfigOfVdc networkConfigOfVdc = vdc.getNetworkConfigOfVdc(Constants.NETWORK_NAME);
		List<String> aIps = networkConfigOfVdc.getAllocatedIpAddresses();
		
		for(String ip : aIps) {
			System.out.println(ip);
		}
		
	}
	
	@Test
	public void testGetIpAddresses() {
		NetworkConfigOfVdc networkConfigOfVdc = vdc.getNetworkConfigOfVdc(Constants.NETWORK_NAME);
		List<String> aIps = networkConfigOfVdc.getAllIpAddresses();
		for(String ip : aIps) {
			System.out.println(ip);
		}
	}

}
