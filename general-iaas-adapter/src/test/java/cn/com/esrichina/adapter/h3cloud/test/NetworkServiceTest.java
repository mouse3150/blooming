package cn.com.esrichina.adapter.h3cloud.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.h3cloud.H3CloudException;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.H3CloudNetwork;
import cn.com.esrichina.adapter.h3cloud.service.H3CNetworkService;

public class NetworkServiceTest {

	private  H3CloudMethod method;
	private H3CNetworkService networkService;
	
	@Before
	public void setUp() throws Exception {
		method = new H3CloudMethod(H3Constants.endpoint, H3Constants.username, H3Constants.password);
		networkService = new H3CNetworkService(method);
	}

	@Test
	public void testGetNetworks() {
		try {
			List<H3CloudNetwork> networks = networkService.getH3CNetworks();
			for(H3CloudNetwork n : networks) {
				System.out.println(n.getName());
				System.out.println(n.getUuid());
			}
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNetworksByCondition() {
		try {
			List<H3CloudNetwork> networks = networkService.getH3CNetworks(0,2);
			for(H3CloudNetwork n : networks) {
				System.out.println(n.getName());
				System.out.println(n.getUuid());
			}
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNetworksById() {
		try {
			H3CloudNetwork network = networkService.getH3CNetworkById(H3Constants.network_id);
			System.out.println(network.getName());
			System.out.println(network.getUuid());
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetAvailableIps() {
		try {
			H3CloudNetwork network = networkService.getH3CNetworkById(H3Constants.network_id);
			System.out.println(network.getName());
			System.out.println(network.getUuid());
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
	}

}
