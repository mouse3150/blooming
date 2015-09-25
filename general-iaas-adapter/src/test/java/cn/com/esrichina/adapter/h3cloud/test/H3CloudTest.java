package cn.com.esrichina.adapter.h3cloud.test;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.IaaSServiceBuilderFactory;
import cn.com.esrichina.adapter.commons.VirtualMachineRuntimeInfo;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.domain.IVirtualMachine;

public class H3CloudTest {

	IaaSService service = null;
	IOrganization org = null;
	IDatacenter datacenter = null;
	@Before
	public void setUp() throws Exception {
		service = new IaaSServiceBuilderFactory(false).createIaaSService(H3Constants.platform, 
				H3Constants.endpoint, H3Constants.org_name, 
			H3Constants.username, H3Constants.password, null); 
		org = service.getOrganizationByName(H3Constants.org_name);
		
		datacenter = org.getDatacenterByName(H3Constants.dc_name);
		
	}

	@Test
	public void getOrgs() {
		try {
			IOrganization[] os = service.getOrganizations();
			for(int i = 0; i < os.length; i++) {
				System.out.println(os[i].getName());
				System.out.println(os[i].getId());
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetOrgByName() {
		try {
			IOrganization os = service.getOrganizationByName(H3Constants.org_name);
			System.out.println(os.getName());
			
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetRuntimeInfo() {
		try {
			IVirtualMachine vm = datacenter.getVirtualMachine(H3Constants.vm_id);
			VirtualMachineRuntimeInfo info = vm.getRuntimeInfo();
			System.out.println(info);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRebootingStatusInfo() {
		try {
			String uuid = H3Constants.vm_id;
			IVirtualMachine vm = datacenter.getVirtualMachine(uuid);
			vm.reboot();
			Thread.sleep(2000L);
			VirtualMachineRuntimeInfo info = vm.getRuntimeInfo();
			System.out.println(info);
		} catch (AdapterException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
