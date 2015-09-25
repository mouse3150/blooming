package cn.com.esrichina.adapter.vcloud.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.DatacenterComputeRuntimeInfo;
import cn.com.esrichina.adapter.commons.DatacenterStorageRuntimeInfo;
import cn.com.esrichina.adapter.commons.GuestCustomizationOptions;
import cn.com.esrichina.adapter.commons.HardwareOptions;
import cn.com.esrichina.adapter.commons.ImageOptions;
import cn.com.esrichina.adapter.commons.LaunchVmOptions;
import cn.com.esrichina.adapter.commons.NetworkConfigOptions;
import cn.com.esrichina.adapter.commons.Platform;
import cn.com.esrichina.adapter.commons.Storage;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IImage;
import cn.com.esrichina.adapter.domain.INetwork;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.domain.IVirtualMachine;
import cn.com.esrichina.adapter.vcloud.VCloudService;

public class VCloudDatacenterTest {
	public static final String SOURCE_SITE = "blank";
	public static final String VM_NAME = "newVm5";
	public static final String DESTINATION_SITE = "blank_to";
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
	public void testGetImages() {
		try {
			IImage[] images = dataCenter.getImages();
			for(IImage image : images) {
				System.out.println(image.getName());
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetImageByName() {
		try {
			IImage image = dataCenter.getImage(Constants.TEMPLATE_NAME);
			System.out.println(image.getName());
			assertEquals(Constants.TEMPLATE_NAME, image.getName());
		} catch (AdapterException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void testGetNetworks() {
		try {
			INetwork[] networks = dataCenter.getNetworks();
			for(INetwork network : networks) {
				System.out.println(network.getName());
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetVirtualMachines() {
		try {
			IVirtualMachine[] vms = dataCenter.getVirtualMachines();
			for(IVirtualMachine vm : vms) {
				System.out.println(vm.getName());
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetVirtualMachinesBySite() {
		try {
			IVirtualMachine[] vms = dataCenter.getVirtualMachines(Constants.SITE_NAME);
			for(IVirtualMachine vm : vms) {
				System.out.println(vm.getName());
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
		
	}

	@Test
	public void testDestroySite() {
		try {
			dataCenter.destroySite(Constants.SITE_NAME);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateSite() {
		NetworkConfigOptions networkConfigOptions = new NetworkConfigOptions();
		
		networkConfigOptions.setNetworkConfigName(Constants.NETWORK_NAME);
		try {
			dataCenter.createSite(Constants.SITE_NAME, networkConfigOptions);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testVMMoveTest() {
		try {
			dataCenter.virtualMachineMove(DESTINATION_SITE, VM_NAME, SOURCE_SITE);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateVmForSite() {
		LaunchVmOptions launchVmOptions = new LaunchVmOptions();
		launchVmOptions.setVmName("newVm5");
		
		NetworkConfigOptions networkConfigOptions = new NetworkConfigOptions();
		networkConfigOptions.setNetworkConfigName(Constants.NETWORK_NAME);
		//vmware network name 用ID处理
		networkConfigOptions.setNetworkConfigId(Constants.NETWORK_NAME);
		networkConfigOptions.setPrivateIpAddress("192.168.190.50");
		
		ImageOptions imageOptions = new ImageOptions();
		//vmware template name 用ID处理
		imageOptions.setImageName(Constants.TEMPLATE_NAME);
		imageOptions.setImageId(Constants.TEMPLATE_NAME);
		imageOptions.setPlatform(Platform.CENT_OS);
		
		HardwareOptions hardwareOptions = new HardwareOptions();
		hardwareOptions.setCpuNum(2);
		hardwareOptions.setCoreNumPerCpu(2);
		hardwareOptions.setMemory(2048);
		
		GuestCustomizationOptions guestCustomizationOptions = new GuestCustomizationOptions();
		guestCustomizationOptions.setComputeName("testgis3");
		guestCustomizationOptions.setPlatform(Platform.CENT_OS);
		guestCustomizationOptions.setAdminPassword("esri@123");
		
		launchVmOptions.setImageOptions(imageOptions);
		launchVmOptions.setNetworkConfigOptions(networkConfigOptions);
		launchVmOptions.setHardwareOptions(hardwareOptions);
		launchVmOptions.setGuestCustomOptions(guestCustomizationOptions);
		try {
			dataCenter.createVirtualMachine(Constants.VAPP_NAME, launchVmOptions);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateVmForSiteAndPowerOn() {
		LaunchVmOptions launchVmOptions = new LaunchVmOptions();
		launchVmOptions.setVmName("newVm5");
		
		NetworkConfigOptions networkConfigOptions = new NetworkConfigOptions();
		networkConfigOptions.setNetworkConfigName(Constants.NETWORK_NAME);
		//vmware network name 用ID处理
		networkConfigOptions.setNetworkConfigId(Constants.NETWORK_NAME);
		networkConfigOptions.setPrivateIpAddress("192.168.190.202");
		
		ImageOptions imageOptions = new ImageOptions();
		//vmware template name 用ID处理
		imageOptions.setImageName(Constants.TEMPLATE_NAME);
		imageOptions.setImageId(Constants.TEMPLATE_NAME);
		imageOptions.setPlatform(Platform.WINDOWS);
		
		HardwareOptions hardwareOptions = new HardwareOptions();
		hardwareOptions.setCpuNum(2);
		hardwareOptions.setCoreNumPerCpu(2);
		hardwareOptions.setMemory(2048);
		
		GuestCustomizationOptions guestCustomizationOptions = new GuestCustomizationOptions();
		guestCustomizationOptions.setComputeName("testgis3");
		guestCustomizationOptions.setPlatform(Platform.WINDOWS);
		guestCustomizationOptions.setAdminPassword("esri@123");
		
		launchVmOptions.setImageOptions(imageOptions);
		launchVmOptions.setNetworkConfigOptions(networkConfigOptions);
		launchVmOptions.setHardwareOptions(hardwareOptions);
		launchVmOptions.setGuestCustomOptions(guestCustomizationOptions);
		IVirtualMachine ivm = null;
		try {
			ivm = dataCenter.createVirtualMachine(Constants.SITE_NAME, launchVmOptions);
			ivm.powerOn();
		} catch (AdapterException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void testGetDCconfig() {
		try {
			dataCenter.getConfiguration();
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDCRuntimeInfo() {
		try {
			DatacenterComputeRuntimeInfo info = dataCenter.getDatacenterComputeRuntimeInfo();
			
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
			List<DatacenterStorageRuntimeInfo> infos = dataCenter.getDatacenterStorageRuntimeInfo();
			
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
