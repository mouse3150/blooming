package cn.com.esrichina.adapter.jcs.openstack.test;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.Constants;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.commons.DatacenterComputeRuntimeInfo;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IImage;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.domain.IVirtualMachine;
import cn.com.esrichina.adapter.jcs.openstack.JcsOpenstackServiceBuilder;

public class JcsOpenstackDatacenterTest {

	private Properties pros;
	private IaaSService service;
	private IOrganization org;
	private IDatacenter datacenter;
	
	@Before
	public void setUp() throws Exception {
		pros = new Properties();
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_ENDPOINT, JcsOpenstackContants.ENDPOINT);
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_REGION, JcsOpenstackContants.REGION_ID);
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_ORG_NAME, JcsOpenstackContants.TENANT_NAME);
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_IDENTIFY, JcsOpenstackContants.ACCESS_IDENTIFY);
		pros.put(JcsOpenstackServiceBuilder.IAAS_PLATFORM + "." + Constants.PROP_KEY_CREDENTIAL, JcsOpenstackContants.ACCESS_PASSWORD);
		
		JcsOpenstackServiceBuilder builder = new JcsOpenstackServiceBuilder(pros);
		
		service = builder.createIaaSService();
		org = service.getCurrentOrganization();
		
		datacenter = org.getDatacenterByName(JcsOpenstackContants.TENANT_NAME + "-DC");
	}

	@Test
	public void testGetDatacenterInfo() {
		try {
			System.out.println("DATACENTER NAME:" + datacenter.getName() + ", DATACENTER ID:" + datacenter.getId());
			System.out.println();
		} catch (AdapterException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetDatacenterVMs() {
		try {
			IVirtualMachine[] vms = datacenter.getVirtualMachines();
			
			if(vms != null) {
				for (int i = 0; i < vms.length; i++) {
					System.out.println(vms[i].getName());
				}
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDatacenterVmsBySite() {
		try {
			IVirtualMachine[] vms = datacenter.getVirtualMachines("site_name");
			
			if(vms != null) {
				for (int i = 0; i < vms.length; i++) {
					System.out.println(vms[i].getName());
				}
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDatacenterVmsByName() {
		try {
			IVirtualMachine vm = datacenter.getVirtualMachine("NFSServer");
			
			System.out.println("VM NAME:" + vm.getName() + ", VM ID:" + vm.getId());
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDatacenterImages() {
		try {
			
			IImage[] images = datacenter.getImages();
			
			for (int i = 0; i < images.length; i++) {
				System.out.println("VM NAME:" + images[i].getName() + ", VM ID:" + images[i].getId());
			}
			
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDatacenterImageByName() {
		try {
			IImage vmImage = datacenter.getImage("e0c348b6-b215-4d22-b3b9-ba01edab3f34");
			
			System.out.println("VM NAME:" + vmImage.getName() + ", VM ID:" + vmImage.getId());
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDatacenterConfigInfo() {
		try {
			DatacenterComputeRuntimeInfo runtimeInfo = datacenter.getDatacenterComputeRuntimeInfo();
			System.out.println(runtimeInfo);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}

}
