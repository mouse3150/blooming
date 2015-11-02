package cn.com.esrichina.adapter.jcs.openstack.test;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.Constants;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.commons.HardwareOptions;
import cn.com.esrichina.adapter.commons.ImageOptions;
import cn.com.esrichina.adapter.commons.LaunchVmOptions;
import cn.com.esrichina.adapter.commons.VirtualMachineRuntimeInfo;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.domain.IVirtualMachine;
import cn.com.esrichina.adapter.jcs.openstack.JcsOpenstackServiceBuilder;

public class JcsOpenstackVirtualMachineTest {

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
	public void testCreateVM() {
		LaunchVmOptions launchVmOptions = new LaunchVmOptions();
		launchVmOptions.setVmName("server001");
		
		HardwareOptions hardoption = new HardwareOptions();
		hardoption.setId(JcsOpenstackContants.FLAVOR_ID);
		
		ImageOptions imageoption = new ImageOptions();
		imageoption.setImageId(JcsOpenstackContants.IMAVE_ID);
		
		launchVmOptions.setHardwareOptions(hardoption);
		launchVmOptions.setImageOptions(imageoption);
		IVirtualMachine vm = null;
		try {
			vm = datacenter.createVirtualMachine("site_name", launchVmOptions);
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println("VM Name:" + vm.getName() + ", VM ID:" + vm.getId() );
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testStopVM() {
		try {
			IVirtualMachine vm = datacenter.getVirtualMachineById(JcsOpenstackContants.VM_ID);
			vm.powerOff();
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testStartVM() {
		try {
			IVirtualMachine vm = datacenter.getVirtualMachineById(JcsOpenstackContants.VM_ID);
			vm.powerOn();
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetVMRuntimeInfo() {
		try {
			IVirtualMachine vm = datacenter.getVirtualMachineById(JcsOpenstackContants.VM_ID);
			VirtualMachineRuntimeInfo runtimeInfo = vm.getRuntimeInfo();
			
			System.out.println(runtimeInfo);
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDelVm() {
		try {
			IVirtualMachine vm = datacenter.getVirtualMachineById(JcsOpenstackContants.VM_ID);
			vm.destroy();
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
