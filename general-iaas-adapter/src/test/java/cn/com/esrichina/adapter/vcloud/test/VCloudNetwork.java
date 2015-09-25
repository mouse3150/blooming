package cn.com.esrichina.adapter.vcloud.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.INetwork;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.vcloud.VCloudService;

public class VCloudNetwork {

	private VCloudService vcloudService;
	private IOrganization org;
	private IDatacenter dataCenter;
	
	@Before
	public void setUp() throws Exception {
		vcloudService = new VCloudService(Constants.URL, Constants.USER, 
				Constants.PASSWORD, "5.5", "all");

		org = vcloudService.getOrganizationByName(Constants.ORG_NAME);
		dataCenter = org.getDatacenterByName(Constants.ORG_VDC_NAME);
	}


	@Test
	public void testGetAllocatedIpAddresses() throws AdapterException {
		INetwork networkConfigOfVdc = dataCenter.getNetworkByName(Constants.NETWORK_NAME);
		List<String> aIps = networkConfigOfVdc.getAllocatedIpAddresses();
		
		for(String ip : aIps) {
			System.out.println(ip);
		}
		
	}
	
	@Test
	public void testGetIpAddresses() throws AdapterException {
		INetwork networkConfigOfVdc = dataCenter.getNetworkByName(Constants.NETWORK_NAME);
		List<String> aIps = networkConfigOfVdc.getAllIpAddresses();
		for(String ip : aIps) {
			System.out.println(ip);
		}
	}
	
	@Test
	public void testGetAvailableIpAddresses() throws AdapterException {
		INetwork networkConfigOfVdc = dataCenter.getNetworkByName(Constants.NETWORK_NAME);
		List<String> aIps = networkConfigOfVdc.getAvailableIpAddresses();
		for(String ip : aIps) {
			System.out.println(ip);
		}
	}

}
