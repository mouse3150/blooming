package cn.com.esrichina.adapter.jcs.openstack;

import java.util.Properties;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.nova.v2_0.NovaApi;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.Constants;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.IaaSServiceBuilder;
import cn.com.esrichina.adapter.utils.Utils;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public class JcsOpenstackServiceBuilder extends  IaaSServiceBuilder {
	private static final String IAAS_PLATFORM = "openstack";
	public JcsOpenstackServiceBuilder(Properties properties) {
		super(properties);
	}
	
	@Override
	public IaaSService createIaaSService() throws AdapterException {
		
		String endpoint = Utils.checkNotNull(
				properties.getProperty(IAAS_PLATFORM + "."
						+ Constants.PROP_KEY_ENDPOINT),
				"Please config the endpoint of the OpenStack!");
		
		String region = Utils.checkNotNull(
				properties.getProperty(IAAS_PLATFORM + "."
						+ Constants.PROP_KEY_REGION),
				"Please config the Region of the OpenStack!");
		
		String tanentname = Utils.checkNotNull(
				properties.getProperty(IAAS_PLATFORM + "."
						+ Constants.PROP_KEY_ORG_NAME),
				"Please config the organization(tenant name) of the OpenStack!");
		
		String username = Utils.checkNotNull(
				properties.getProperty(IAAS_PLATFORM + "."
						+ Constants.PROP_KEY_IDENTIFY),
				"Please config the identify(username) of the OpenStack!");
		
		String password = Utils.checkNotNull(
				properties.getProperty(IAAS_PLATFORM + "."
						+ Constants.PROP_KEY_CREDENTIAL),
				"Please config the credential(password) of the OpenStack!");
		
		Iterable<Module> modules = ImmutableSet.<Module>of(new SLF4JLoggingModule());

        String provider = "openstack";
        String identity = tanentname + ":" + username; // tenantName:userName

        NovaApi novaApi = ContextBuilder.newBuilder(provider)
                .endpoint(endpoint)
                .credentials(identity, password)
                .modules(modules)
                .buildApi(NovaApi.class);
        Set<String> regions = novaApi.getConfiguredRegions();
		
        //check region existed.
        if(!regions.contains(region)) {
        	throw new AdapterException("Region:'" + region + "' is not existed. please check it." );
        }
        
        IaaSService iaaSServcie = new JcsOpenstackService(region, novaApi);
		return iaaSServcie;
	}
}
