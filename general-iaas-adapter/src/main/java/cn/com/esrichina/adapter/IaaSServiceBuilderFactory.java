package cn.com.esrichina.adapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Create a IaaSService instance for a special IaaS Platform.
 * 
 * @author Esri
 *
 */
public class IaaSServiceBuilderFactory {
	private static final String CONFIG_FILE = "/iaas.properties";
	private static final String IAAS_TYPE = "iaas.type";

	private static final String IAAS_PLATFORM_SERVICEBUILD = "serviceBuilder";

	private static final List<String> SUPPORTIAAS = Arrays.asList("vcloud","h3cloud");
	
	private static final Map<String, String> builders;

	private Properties properties;

	static {
		builders = new HashMap<String, String>();
		builders.put("vcloud", "cn.com.esrichina.adapter.vcloud.VCloudServiceBuilder");
		builders.put("h3cloud", "cn.com.esrichina.adapter.h3cloud.H3CloudServiceBuilder");
	}
	/**
	 * 
	 * @param useDefaultConfig
	 * 
	 */
	public IaaSServiceBuilderFactory(boolean useDefaultConfig) {
		if(useDefaultConfig) {
			properties = loadPropertiesFromConfigFile();
		} 
	}
	
	public IaaSServiceBuilderFactory(Properties pros) {
		this.properties = pros;
		//properties = loadPropertiesFromConfigFile();
	}

	/**
	 * @param iaasType
	 *            IaaS Platform type, e.g. vCloud
	 * @param identify
	 *            Identify of the IaaS Platform (usually is a username)
	 * @param credential
	 *            Credential of the IaaS Platform (usually is a password)
	 * @return IaaSService identified by the <code>iaasType</code>.
	 */
	public IaaSServiceBuilder createIaaSServiceBuilder(String iaasType, String endpoint, String orgName, String identify,
			String credential) {
		return createIaaSServiceBuilder(iaasType, endpoint, orgName, identify, credential, null);
	}

	/**
	 * @param iaasType
	 *            IaaS Platform type, e.g. vCloud
	 * @param identify
	 *            Identify of the IaaS Platform (usually is a username)
	 * @param credential
	 *            Credential of the IaaS Platform (usually is a password)
	 * @param overrides
	 *            Properties to override the default property values
	 * @return IaaSService identified by the <code>iaasType</code>.
	 */
	public IaaSServiceBuilder createIaaSServiceBuilder(String iaasType, String endpoint, String orgName, String identify,
			String credential, Properties extension) {
		
		if(properties == null) {
			properties = new Properties();
		}
		
		if(extension != null) {
			properties.putAll(extension);
		}

		if (!SUPPORTIAAS.contains(iaasType.toLowerCase())) {
			throw new AdapterRuntimeException("Can not support " + iaasType);
		}

		properties.put(iaasType + "." + IAAS_PLATFORM_SERVICEBUILD, (String) builders.get(iaasType));
		properties.put(IAAS_TYPE, iaasType);
		properties.put(iaasType + "." + Constants.PROP_KEY_ENDPOINT, endpoint);
		properties.put(iaasType + "." + Constants.PROP_KEY_ORG_NAME, orgName);
		properties.put(iaasType + "." + Constants.PROP_KEY_IDENTIFY, identify);
		properties.put(iaasType + "." + Constants.PROP_KEY_CREDENTIAL, credential);
		

		String iaasServiceBuilder = properties.getProperty(iaasType + "." + IAAS_PLATFORM_SERVICEBUILD);
		
		if (iaasServiceBuilder == null) {
			throw new AdapterRuntimeException("Do not config " + "'iaasType." + "serviceBuilder' in the property file:"
					+ CONFIG_FILE);
		}

		IaaSServiceBuilder serviceBuilder = createIaaSServiceBuilder(properties);
		
		return serviceBuilder;
	}
	
	public IaaSServiceBuilder createIaaSServiceBuilder(Properties properties) {
		
		String iaasType = properties.getProperty(IAAS_TYPE);
		if(iaasType == null || "".equalsIgnoreCase(iaasType)) {
			throw new AdapterRuntimeException("No specify iaas type");
		}
		String iaasServiceBuilder = properties.getProperty(iaasType + "." + IAAS_PLATFORM_SERVICEBUILD);
		
		if (iaasServiceBuilder == null) {
			throw new AdapterRuntimeException("No config " + "'iaasType." + "serviceBuilder' in the property file:"
					+ CONFIG_FILE);
		}

		IaaSServiceBuilder serviceBuilder = null;
		try {
			@SuppressWarnings("unchecked")
			Class<IaaSServiceBuilder> serviceBuilderClass = (Class<IaaSServiceBuilder>) Class
					.forName(iaasServiceBuilder);

			serviceBuilder = serviceBuilderClass.getConstructor(Properties.class).newInstance(properties);
		} catch (Exception e) {
			throw new AdapterRuntimeException("Can not construct " + iaasServiceBuilder + " instance", e);
		}
		return serviceBuilder;
	}

	public IaaSService createIaaSService(String iaasType, String endpoint, String orgName, String identify, String credential,
			Properties overrides) throws AdapterException {
		IaaSServiceBuilder serviceBuilder = createIaaSServiceBuilder(iaasType, endpoint, orgName, identify, credential,
				overrides);
		return serviceBuilder.createIaaSService();
	}

	private Properties loadPropertiesFromConfigFile() {
		return loadPropertiesFromConfigFile(CONFIG_FILE);
	}

	private Properties loadPropertiesFromConfigFile(String filename) {
		Properties properties = new Properties();
		try {
			properties.load(IaaSServiceBuilderFactory.class.getResourceAsStream(filename));
		} catch (IOException e) {
			throw new AdapterRuntimeException("Can not load property file " + filename, e);
		}
		return properties;
	}
}
