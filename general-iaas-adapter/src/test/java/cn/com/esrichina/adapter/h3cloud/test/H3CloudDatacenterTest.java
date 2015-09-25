package cn.com.esrichina.adapter.h3cloud.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.DatacenterComputeRuntimeInfo;
import cn.com.esrichina.adapter.commons.DatacenterStorageRuntimeInfo;
import cn.com.esrichina.adapter.commons.Storage;
import cn.com.esrichina.adapter.domain.IImage;
import cn.com.esrichina.adapter.domain.INetwork;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.H3CloudService;
import cn.com.esrichina.adapter.h3cloud.domain.H3CloudDatacenter;
import cn.com.esrichina.adapter.h3cloud.domain.H3CloudOrganization;

public class H3CloudDatacenterTest {
	
	private H3CloudMethod method;
	private H3CloudService service;
	private H3CloudOrganization org;
	private H3CloudDatacenter datacenter;

	@Before
	public void setUp() throws Exception {
		method = new H3CloudMethod(H3Constants.endpoint, H3Constants.username, H3Constants.password);
		service = new H3CloudService(method);
		org = (H3CloudOrganization) service.getOrganizationByName(H3Constants.org_name);
		datacenter = (H3CloudDatacenter) org.getDatacenterByName(H3Constants.dc_name);
	}
	
	@Test
	public void test() {
		try {
			System.out.println(datacenter.getName());
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetImages() {
		try {
			IImage[] images = datacenter.getImages();
			
			if(images != null) {
				for(int i = 0; i < images.length; i++) {
					System.out.println(images[i].getName() + " : " + images[i].getId());
				}
			}
			
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetImageById() {
		try {
			IImage image = datacenter.getImage(H3Constants.image_id);
			
			System.out.println(image.getName() + " : " + image.getId());
			
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNetworks() {
		try {
			INetwork[] networks = datacenter.getNetworks();
			
			if(networks != null) {
				for(int i = 0; i < networks.length; i++) {
					System.out.println(networks[i].getName() + " : " + networks[i].getId());
				}
			}
			
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNetworkById() {
		try {
			INetwork network = datacenter.getNetworkByName(H3Constants.network_id);
			
			if(network != null) {
				System.out.println(network.getName() + " : " + network.getId());
			}
			
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAvailableIps() {
		try {
			INetwork network = datacenter.getNetworkByName(H3Constants.network_id);
			
			if(network != null) {
				System.out.println(network.getName() + " : " + network.getId());
			}
			
			List<String> ips = network.getAvailableIpAddresses();
			System.out.println("可用IP地址:" + ips.size());
			for(String ip : ips) {
				System.out.println(ip);
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testAllIps() {
		try {
			INetwork network = datacenter.getNetworkByName(H3Constants.network_id);
			
			if(network != null) {
				System.out.println(network.getName() + " : " + network.getId());
			}
			
			List<String> ips = network.getAllIpAddresses();
			System.out.println("所有IP地址:" + ips.size());
			System.out.println(ips);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAllocateIps() {
		try {
			INetwork network = datacenter.getNetworkByName(H3Constants.network_id);
			
			if(network != null) {
				System.out.println(network.getName() + " : " + network.getId());
			}
			
			List<String> ips = network.getAllocatedIpAddresses();
			System.out.println("已分配IP地址:" + ips.size());
			System.out.println(ips);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDCRuntimeInfo() {
		try {
			DatacenterComputeRuntimeInfo info = datacenter.getDatacenterComputeRuntimeInfo();
			
			long totalValue = info.getTotalCpu().getValue();
			System.out.println(totalValue);
			long usedValue = info.getUsedCpu().getValue();
			System.out.println(usedValue);
			
			long totalMem = info.getTotalMemory().getValue();
			long usedMem = info.getUsedMemory().getValue();
			System.out.println(totalMem);
			System.out.println(usedMem);
			
			System.out.println(info.isLimited());
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDcStorageRuntimeInfo() {
		try {
			List<DatacenterStorageRuntimeInfo> infos = datacenter.getDatacenterStorageRuntimeInfo();
			
			//换算成GB
			long totalStorage = 0;
			long usedStorage = 0;
			for(DatacenterStorageRuntimeInfo info : infos) {
				Storage totalsto = info.getTotal();
				Storage usedsto = info.getUsed();
				totalStorage += totalsto.getUnit().toGigaBytes(totalsto.getValue());
				usedStorage += usedsto.getUnit().toGigaBytes(usedsto.getValue());
			}
			System.out.println("totalStorage:" + totalStorage);
			System.out.println("usedStorage:" + usedStorage);
			System.out.println();
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
