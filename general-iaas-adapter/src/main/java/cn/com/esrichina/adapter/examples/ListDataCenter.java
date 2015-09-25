package cn.com.esrichina.adapter.examples;

import java.io.IOException;
import java.util.Properties;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.AdapterRuntimeException;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.IaaSServiceBuilder;
import cn.com.esrichina.adapter.IaaSServiceBuilderFactory;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IImage;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.domain.IVirtualMachine;

public class ListDataCenter {
	private static final String IAAS_CONF_NAME = "/iaas.properties";

	@SuppressWarnings("unused")
	public static void main(String[] args) throws AdapterException {
		IaaSServiceBuilderFactory iaasFactory = new IaaSServiceBuilderFactory(false);
		
		Properties properties = loadProperties();
		
		IaaSServiceBuilder iaasBuilder = iaasFactory.createIaaSServiceBuilder(properties);
		IaaSService service = iaasBuilder.createIaaSService();
		try {
			
			service.connect();
			String name = service.getCloudName();
			System.out.println(name);
			IOrganization[] orgs = service.getOrganizations();
			if(orgs != null) {
				for(IOrganization org : orgs) {
					System.out.println("org name is:" + org.getName());
					IDatacenter[] dcs = org.getDatacenters();
					for(IDatacenter dc : dcs) {
						//获取Datacenter中 虚拟机镜像
						IImage[] imges = dc.getImages();
						//获取Datacener中 虚拟机
						IVirtualMachine[] vms = dc.getVirtualMachines();
					}
				}
			}
			
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	public static Properties loadProperties() {
		
		Properties properties = new Properties();
		try {
			properties.load(ListDataCenter.class.getResourceAsStream(IAAS_CONF_NAME));
		} catch (IOException e) {
			throw new AdapterRuntimeException("Can not load property file " + IAAS_CONF_NAME, e);
		}
		return properties;
	}

}
