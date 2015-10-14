package cn.com.esrichina.adapter.openstack.test;

import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.openstack.AuthenticationContext;
import cn.com.esrichina.adapter.openstack.NovaMethod;
import cn.com.esrichina.adapter.openstack.NovaOpenStack;
import cn.com.esrichina.adapter.openstack.OpenStackProvider;
import cn.com.esrichina.adapter.openstack.ProviderContext;

public class NovaOpenStackTest {
	NovaOpenStack openstack = null;
	@Before
	public void setUp() throws Exception {
		
		ProviderContext context = new ProviderContext();
		
		context.setEndpoint(OpenStackContanst.ENDPOINT);
		context.setAccountNumber(OpenStackContanst.ACCOUNT_NAME);
		context.setRegionId(OpenStackContanst.REGION_ID);
		context.setAccessPublic(OpenStackContanst.ACCESS_PUBLIC);
		context.setAccessPrivate(OpenStackContanst.ACCESS_PRIVATE);
		context.setProvider(OpenStackProvider.OPENSTACK);
		openstack = new NovaOpenStack(context);
	}

	@Test
	public void isSupportedVersionTest() {
		String testVersion = "2.0";
		boolean support = NovaOpenStack.isSupported(testVersion);
		System.out.println(support ? "suport v" + testVersion : "do not suport v" + testVersion);
		assertTrue(support);
	}
	
	@Test
	public void getAuthenticationContextTest() {
		try {
			AuthenticationContext authContext = openstack.getAuthenticationContext();
			System.out.println("Token:" + authContext.getAuthToken());
			System.out.println("ComputeUrl:" + authContext.getComputeUrl());
			System.out.println("Region:" + authContext.getMyRegion());
			System.out.println("NetworkUrl:" + authContext.getNetworkUrl());
			System.out.println("TenantId:" + authContext.getTenantId());
			System.out.println("StorageToken:" + authContext.getStorageToken());
			System.out.println("StorageUrl:" + authContext.getStorageUrl());
			System.out.println("Compute ServiceUrl:" + authContext.getServiceUrl("compute"));
			
			//test getAuthenticationContext from cache.
			AuthenticationContext authContext2 = openstack.getAuthenticationContext();
			
			System.out.println("Token:" + authContext2.getAuthToken());
			System.out.println("ComputeUrl:" + authContext2.getComputeUrl());
			System.out.println("Region:" + authContext2.getMyRegion());
			System.out.println("NetworkUrl:" + authContext2.getNetworkUrl());
			System.out.println("TenantId:" + authContext2.getTenantId());
			System.out.println("StorageToken:" + authContext2.getStorageToken());
			System.out.println("StorageUrl:" + authContext2.getStorageUrl());
			System.out.println("Compute ServiceUrl:" + authContext2.getServiceUrl("compute"));
			
			//test token expire.
			NovaMethod method = new NovaMethod(openstack);
			
			JSONObject js = method.getServers("/servers", "be40deba-3fbb-4d79-8cab-d9c005212034", true);
//			System.out.println(js);
//			try {
//				Thread.sleep(60000L * 30);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
			JSONObject js2 = method.getServers("/servers", "be40deba-3fbb-4d79-8cab-d9c005212034", true);
			System.out.println(js2);
			
			
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}

}
