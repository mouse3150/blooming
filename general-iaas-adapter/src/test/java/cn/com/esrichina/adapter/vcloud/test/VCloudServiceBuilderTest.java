package cn.com.esrichina.adapter.vcloud.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.AdapterRuntimeException;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.examples.ListDataCenter;
import cn.com.esrichina.adapter.vcloud.VCloudServiceBuilder;

public class VCloudServiceBuilderTest {
	private static final String CLOUD_NAME = "vcloud";
	private static final String CLOUD_PROVIDER_NAME = "VMware";
	private static final String CLOUD_VERSION = "5.5";
	private static final String IAAS_CONF_NAME = "iaas.properties";
	private IaaSService iaasService;
	@Before
	public void setUp() throws Exception {
		VCloudServiceBuilder vsb = new VCloudServiceBuilder(loadProperties());
		iaasService = vsb.createIaaSService();
	}

	@Test
	public void initServiceByBuilder() {
		try {
			assertEquals(CLOUD_NAME, iaasService.getCloudName());
			assertEquals(CLOUD_PROVIDER_NAME, iaasService.getProviderName());
			assertEquals(CLOUD_VERSION, iaasService.getCloudVersion());
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetOrganizations() {
		try {
			IOrganization[] orgs = iaasService.getOrganizations();
			for(IOrganization org : orgs) {
				org.getName();
				System.out.println(org.getName());
				assertEquals(Constants.ORG_NAME, org.getName());
			}
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetOrganizationByName() {
		try {
			IOrganization org = iaasService.getOrganizationByName(Constants.ORG_NAME);
			System.out.println(org.getName());
			assertEquals(Constants.ORG_NAME, org.getName());
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
	
	public Properties loadProperties() {
		
		Properties properties = new Properties();
		try {
			properties.load(ListDataCenter.class.getResourceAsStream(IAAS_CONF_NAME));
		} catch (IOException e) {
			throw new AdapterRuntimeException("Can not load property file " + IAAS_CONF_NAME, e);
		}
		return properties;
	}

}
