package cn.com.esrichina.adapter.h3cloud;

import java.util.Properties;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.Constants;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.IaaSServiceBuilder;
import cn.com.esrichina.adapter.utils.Utils;

public class H3CloudServiceBuilder extends IaaSServiceBuilder {
	private static final String IAAS_PLATFORM = "h3cloud";
	
	public H3CloudServiceBuilder(Properties properties) {
		super(properties);
	}
	
	@Override
	public IaaSService createIaaSService() throws AdapterException {
		
		String endpoint = Utils.checkNotNull(
				properties.getProperty(IAAS_PLATFORM + "."
						+ Constants.PROP_KEY_ENDPOINT),
				"Please config the endpoint of the H3Cloud!");
		String username = Utils.checkNotNull(
				properties.getProperty(IAAS_PLATFORM + "."
						+ Constants.PROP_KEY_IDENTIFY),
				"Please config the identify(username) of the H3Cloud!");
		String password = Utils.checkNotNull(
				properties.getProperty(IAAS_PLATFORM + "."
						+ Constants.PROP_KEY_CREDENTIAL),
				"Please config the credential(password) of the H3Cloud!");
		IaaSService iaaSServcie = new H3CloudService(endpoint, username, password);
		
		return iaaSServcie;
	}
}
