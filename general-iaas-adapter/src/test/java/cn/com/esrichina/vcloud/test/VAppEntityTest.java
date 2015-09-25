package cn.com.esrichina.vcloud.test;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.GuestCustomizationOptions;
import cn.com.esrichina.adapter.commons.HardwareOptions;
import cn.com.esrichina.adapter.commons.ImageOptions;
import cn.com.esrichina.adapter.commons.LaunchVmOptions;
import cn.com.esrichina.adapter.commons.NetworkConfigOptions;
import cn.com.esrichina.adapter.commons.Platform;
import cn.com.esrichina.vcloud.OrgEntity;
import cn.com.esrichina.vcloud.VAppEntity;
import cn.com.esrichina.vcloud.VcloudService;
import cn.com.esrichina.vcloud.VdcEntity;

import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.VM;
import com.vmware.vcloud.sdk.VappTemplate;

public class VAppEntityTest {
	public static final String TEMPLATE_NAME = "wa1022windows";
	public static final String VAPP_NAME = "for-test";

	private static VcloudService vcloudService = null;
	private static OrgEntity org = null;
	private static VdcEntity vdc = null;
	private static VAppEntity vapp = null;

	@Before
	public void setUp() throws Exception {
		vcloudService = new VcloudService(Constants.URL, Constants.USER, Constants.PASSWORD);
		org = vcloudService.getOrgByName(Constants.ORG_NAME);
		vdc = org.getVdcByName(Constants.ORG_VDC_NAME);
		vapp = vdc.getVappByName("blank");
	}

	@Test
	public void testCreateBlankVapp() {
		vdc.createBlankVapp("blank", vdc.getNetworkConfigOfVdc(Constants.NETWORK_NAME));
	}

	@Test
	public void testCreateVm() {
		VappTemplate vapptemp = vdc.getVappTemplateByName(TEMPLATE_NAME);
		vapp.createVM("newVm", vdc.getNetworkConfigOfVdc("adorgvdcnet"), vapptemp);
	}

	@Test
	public void testCreateVmByOpt() {
		LaunchVmOptions options = new LaunchVmOptions();

		ImageOptions imageOptions = new ImageOptions();
		HardwareOptions hardwareOptions = new HardwareOptions();
		NetworkConfigOptions networkConfigOptions = new NetworkConfigOptions();
		GuestCustomizationOptions guestCustomOptions = new GuestCustomizationOptions();

		options.setVmName("newVm");

		// template configuration
		imageOptions.setImageName(TEMPLATE_NAME);
		imageOptions.setPlatform(Platform.WINDOWS);
		options.setImageOptions(imageOptions);

		// hardware configuration
		hardwareOptions.setCpuNum(2);
		hardwareOptions.setMemory(2048);
		options.setHardwareOptions(hardwareOptions);

		// network configuration
		networkConfigOptions.setPublicIpAddress("192.168.190.232");
		networkConfigOptions.setNetworkConfigName(Constants.NETWORK_NAME);
		options.setNetworkConfigOptions(networkConfigOptions);

		guestCustomOptions.setAdminPassword("esri@123");
		guestCustomOptions.setComputeName("hostname");
		options.setGuestCustomOptions(guestCustomOptions);

		try {
			vapp.createVM(options);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateVmByOpt() {
		LaunchVmOptions options = new LaunchVmOptions();

		ImageOptions imageOptions = new ImageOptions();
		HardwareOptions hardwareOptions = new HardwareOptions();
		NetworkConfigOptions networkConfigOptions = new NetworkConfigOptions();
		GuestCustomizationOptions guestCustomOptions = new GuestCustomizationOptions();

		options.setVmName("newVm");

		// template configuration
		imageOptions.setImageName(TEMPLATE_NAME);
		imageOptions.setPlatform(Platform.WINDOWS);
		options.setImageOptions(imageOptions);

		// hardware configuration
		hardwareOptions.setCpuNum(2);
		hardwareOptions.setMemory(2048);
		options.setHardwareOptions(hardwareOptions);

		// network configuration
		networkConfigOptions.setPublicIpAddress("192.168.190.232");
		networkConfigOptions.setNetworkConfigName(Constants.NETWORK_NAME);
		options.setNetworkConfigOptions(networkConfigOptions);

		guestCustomOptions.setAdminPassword("esri@123");
		guestCustomOptions.setComputeName("hostname");
		options.setGuestCustomOptions(guestCustomOptions);

		vapp.updateVM("newVm", options);
	}

	@Test
	public void testPowerOn() {
		vapp.powerOn();
	}

	@Test
	public void testStart() {
		vapp.start();
	}

	@Test
	public void testStop() {
		vapp.stop();
	}

	@Test
	public void testPowerOff() {
		vapp.powerOff();
	}

	@Test
	public void testShutdown() {
		vapp.shutdown();
	}

	@Test
	public void testUndeploy() {
		vapp.undeploy();
	}

	@Test
	public void testDelete() {
		vapp.delete();
	}

	@Test
	public void testGetVmByName() {
		VM vm = vapp.getVMbyName("newVm");
		try {
			Map<Integer, String> ip = vm.getIpAddressesById();
			System.out.println(ip);
		} catch (VCloudException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
