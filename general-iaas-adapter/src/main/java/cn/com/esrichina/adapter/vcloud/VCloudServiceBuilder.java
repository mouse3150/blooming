package cn.com.esrichina.adapter.vcloud;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.Constants;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.IaaSServiceBuilder;
import cn.com.esrichina.adapter.utils.Utils;

public class VCloudServiceBuilder extends IaaSServiceBuilder {
	
	private static final String IAAS_PLATFORM = "vcloud";
	
	public VCloudServiceBuilder(Properties properties) {
		super(properties);
	}
	
	@Override
	public IaaSService createIaaSService() throws AdapterException {
		String endpoint = Utils.checkNotNull(
				properties.getProperty(IAAS_PLATFORM + "."
						+ Constants.PROP_KEY_ENDPOINT),
				"Please config the endpoint of the vCloud!");
		String username = Utils.checkNotNull(
				properties.getProperty(IAAS_PLATFORM + "."
						+ Constants.PROP_KEY_IDENTIFY),
				"Please config the identify(username) of the vCloud!");
		String password = Utils.checkNotNull(
				properties.getProperty(IAAS_PLATFORM + "."
						+ Constants.PROP_KEY_CREDENTIAL),
				"Please config the credential(password) of the vCloud!");
		
		String orgnization  = Utils.checkNotNull(
				properties.getProperty(VCloudConstants.VCLOUD_PRO_KEY_ORG),
				"Please config the orgnization of the vCloud!");
		
		String insecure  =  properties.getProperty(VCloudConstants.VCLOUD_PRO_KEY_INSECURE);
		
		String version = properties.getProperty(VCloudConstants.VCLOUD_PRO_KEY_VSERSION);
		String logLevel = properties.getProperty(VCloudConstants.VCLOUD_PRO_KEY_LOGLEVEL);
		
		InputStream is = VCloudService.class.getResourceAsStream("/cn/com/esrichina/adapter/vcloud/vcloud.conf");
		
		Properties prop = new Properties();
		try {
			prop.load(is);
			if(insecure == null) {
				insecure = prop.getProperty(VCloudConstants.VCLOUD_PRO_KEY_INSECURE);
			}
			
			if(version == null) {
				version = prop.getProperty(VCloudConstants.VCLOUD_PRO_KEY_VSERSION);
			}
			
			if(logLevel == null) {
				logLevel = prop.getProperty(VCloudConstants.VCLOUD_PRO_KEY_LOGLEVEL);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new AdapterException("File '/cn/com/esrichina/adapter/vcloud/vcloud.conf' load error.");
		}
		
		
//		VcloudClient.setLogLevel(Level.ALL);
//		
//		
//		vcloudClient = new VcloudClient(endpoint, vCloudVersion);
//		try {
//			vcloudClient.registerScheme("https", 443, FakeSSLSocketFactory
//					.getInstance());
//		} catch (KeyManagementException e) {
//			e.printStackTrace();
//		} catch (UnrecoverableKeyException e) {
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (KeyStoreException e) {
//			e.printStackTrace();
//		}
		String userOrg = username + "@" + orgnization;
		
		return new VCloudService(endpoint, userOrg, password, version, logLevel);
	}
}
