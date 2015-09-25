package cn.com.esrichina.adapter.vcloud.test;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.OsInfo;
import cn.com.esrichina.adapter.commons.VirtualMachineConfiguration;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.domain.IVirtualMachine;
import cn.com.esrichina.adapter.vcloud.VCloudService;

public class VCloudVirtualMachineTest {

	private VCloudService vcloudService;
	private IOrganization org;
	private IDatacenter dataCenter;
	private IVirtualMachine vm;
	
	@Before
	public void setUp() throws Exception {
		vcloudService = new VCloudService(Constants.URL, Constants.USER, Constants.PASSWORD, "5.5", "all"); 
		org = vcloudService.getOrganizationByName(Constants.ORG_NAME);
		dataCenter = org.getDatacenterByName(Constants.ORG_VDC_NAME);
		vm = dataCenter.getVirtualMachine(Constants.SITE_NAME, Constants.VM_ID);
	}

	@Test
	public void testDestroyVm() {
		try {
			vm.destroy();
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPowerOffVm() {
		try {
			vm.powerOff();
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPowerOnVm() {
		try {
			vm.powerOn();
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetOsInfo() {
		try {
			OsInfo os = vm.getOs();
			System.out.println(os.getName());
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetConfiguration() {
		
		 try {
			VirtualMachineConfiguration config = vm.getConfiguration();
			
			System.out.println(config);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
		 
	 }
	
	@Test
	public void testGetNetworkId() {
		try {
			String network = vm.getNetworkId();
			System.out.println(network);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}

}
