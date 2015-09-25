package cn.com.esrichina.adapter.vcloud;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.commons.VmFlavor;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.utils.FakeSSLSocketFactory;
import cn.com.esrichina.adapter.vcloud.domain.VCloudOrganization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.sdk.Organization;
import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.constants.Version;

public class VCloudService implements IaaSService {
	private Logger logger = (Logger) LoggerFactory.getLogger(VCloudService.class);
	//private Logger logger = Logger.getLogger(VCloudService.class);
	private static final String URL = "https://192.168.190.83";
	private static final String USER = "admin@liucy";
	private static final String PASSWORD = "esri@123";
	private static final Level DEFAULT_LOG_LEVLE = Level.ALL;

	private VcloudClient vcloudClient;
	private String cloudName = "vCloud";
	private String cloudVersion;
	private String cloudProvider = "VMware";

	private static List<VmFlavor> vmFlavors = new ArrayList<VmFlavor>();

	static {
		ObjectMapper mapper = new ObjectMapper();
		InputStream is = VCloudService.class.getResourceAsStream("/cn/com/esrichina/adapter/vcloud/vmware_vmflavors.json");
		try {
			JsonNode node = mapper.readTree(is);
			if (node.isArray()) {
				Iterator<JsonNode> its = node.elements();
				while (its.hasNext()) {
					JsonNode typeNode = its.next();
					String name = typeNode.get("name").asText();
					String id = typeNode.get("id").asText();
					int cpu = typeNode.get("cpu").asInt();
					int memory = typeNode.get("memory").asInt();
					int disk = typeNode.get("disk").asInt();
					VmFlavor vmFlavor = new VmFlavor();
					vmFlavor.setName(name);
					vmFlavor.setId(id);
					vmFlavor.setVcpu(cpu);
					vmFlavor.setDisk(disk);
					vmFlavor.setMemory(memory);
					vmFlavors.add(vmFlavor);
				}
			}

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public VCloudService(VcloudClient vcloudClient) {
		super();
		this.vcloudClient = vcloudClient;
	}

	private String user;
	private String password;
	public VCloudService() throws AdapterException {
		VcloudClient.setLogLevel(DEFAULT_LOG_LEVLE);
		vcloudClient = new VcloudClient(URL, Version.V5_5);

		try {
			vcloudClient.registerScheme("https", 443, FakeSSLSocketFactory.getInstance());
			
			vcloudClient.login(USER, PASSWORD);
			this.user = USER;
			this.password = PASSWORD;
		} catch (KeyManagementException e) {
			logger.error("User:" + user + " login error: " + e.getMessage());
			e.printStackTrace();
			throw new AdapterException(e.getMessage());
		} catch (UnrecoverableKeyException e) {
			logger.error("User:" + user + " login error: " + e.getMessage());
			e.printStackTrace();
			throw new AdapterException(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			logger.error("User:" + user + " login error: " + e.getMessage());
			e.printStackTrace();
			throw new AdapterException(e.getMessage());
		} catch (KeyStoreException e) {
			logger.error("User:" + user + " login error: " + e.getMessage());
			e.printStackTrace();
			throw new AdapterException(e.getMessage());
		} catch (VCloudException e) {
			logger.error("User:" + user + " login error: " + e.getMessage());
			e.printStackTrace();
			throw new AdapterException(e.getMessage());
		}
	}

	public VCloudService(String url, String user, String password, String version, String logLevel) throws AdapterException {

		Version vCloudVersion = null;
		if ("5.5".equals(version)) {
			vCloudVersion = Version.V5_5;
		} else if ("5.1".equals(version)) {
			vCloudVersion = Version.V5_1;
		} else if ("1.5".equals(version)) {
			vCloudVersion = Version.V1_5;
		} else {
			vCloudVersion = Version.V5_5;
		}

		Level level = null;

		if ("warning".equalsIgnoreCase(logLevel)) {
			level = Level.WARNING;
		} else if ("all".equalsIgnoreCase(logLevel)) {
			level = Level.ALL;
		} else if ("info".equalsIgnoreCase(logLevel)) {
			level = Level.INFO;
		} else if ("severe".equalsIgnoreCase(logLevel)) {
			level = Level.SEVERE;
		} else if ("off".equalsIgnoreCase(logLevel)) {
			level = Level.OFF;
		} else {
			level = Level.ALL;
		}

		VcloudClient.setLogLevel(level);
		vcloudClient = new VcloudClient(url, vCloudVersion);
		vcloudClient.setConnectionTimeout(60 * 1000);
		vcloudClient.setSocketTimeout(60 * 1000);
		try {
			vcloudClient.registerScheme("https", 443, FakeSSLSocketFactory.getInstance());
			vcloudClient.login(user, password);
			this.user = user;
			this.password = password;
		} catch (KeyManagementException e) {
			logger.error("User:" + user + " login error: " + e.getMessage());
			e.printStackTrace();
			throw new AdapterException(e.getMessage());
		} catch (UnrecoverableKeyException e) {
			logger.error("User:" + user + " login error: " + e.getMessage());
			e.printStackTrace();
			throw new AdapterException(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			logger.error("User:" + user + " login error: " + e.getMessage());
			e.printStackTrace();
			throw new AdapterException(e.getMessage());
		} catch (KeyStoreException e) {
			logger.error("User:" + user + " login error: " + e.getMessage());
			e.printStackTrace();
			throw new AdapterException(e.getMessage());
		} catch (VCloudException e) {
			logger.error("User:" + user + " login error: " + e.getMessage());
			e.printStackTrace();
			throw new AdapterException(e.getMessage());
		}
	}

	@Override
	public void connect() throws AdapterException {
		if (vcloudClient != null) {
			logger.info("VMware User:" + vcloudClient.getUserName() + ", " + vcloudClient.getVcloudApiURL() + " was connected.");
		}
	}

	@Override
	public void extendSession() throws AdapterException {
		logger.info("VMware User:" + vcloudClient.getUserName() + ", " + vcloudClient.getVcloudApiURL() + ", Relogin.");
		//reload
		try {
			//vcloudClient.getOrgRefsByName();
			vcloudClient.login(user, password);
		} catch (VCloudException e) {
			throw new AdapterException(e);
		}
		//vcloudClient.extendSession();
	}

	@Override
	public boolean isConnected() throws AdapterException {
		return true;
	}

	@Override
	public void disConnect() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCloudName() throws AdapterException {
		return cloudName == null ? "vCloud" : cloudName;
	}

	@Override
	public String getCloudVersion() throws AdapterException {
		return cloudVersion == null ? "1.5" : cloudVersion;
	}

	@Override
	public String getProviderName() throws AdapterException {
		return cloudProvider == null ? "VMware" : cloudProvider;
	}

	@Override
	public IOrganization getOrganizationByName(String name) throws AdapterException {
		if (name == null) {
			throw new RuntimeException("vCloud Organization can not be null.");
		}
		for (IOrganization org : getOrganizations()) {
			if (name.equals(org.getName())) {
				return org;
			}
		}
		return null;
	}

	@Override
	public IOrganization[] getOrganizations() {
		HashMap<String, ReferenceType> orgsList;
		IOrganization[] orgs = null;
		try {
			orgsList = vcloudClient.getOrgRefsByName();

			if (orgsList != null) {
				orgs = new VCloudOrganization[orgsList.size()];
				int i = 0;
				for (ReferenceType orgRef : orgsList.values()) {
					Organization org = Organization.getOrganizationByReference(vcloudClient, orgRef);
					VCloudOrganization vOrg = new VCloudOrganization(vcloudClient, org);
					orgs[i] = vOrg;
					i++;
				}
			}
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return orgs;
	}

	@Override
	public IOrganization getCurrentOrganization() throws AdapterException {
		return null;
	}

	@Override
	public List<VmFlavor> getVmFlavors() throws AdapterException {
		if (vmFlavors.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			InputStream is = VCloudService.class.getResourceAsStream("/cn/com/esrichina/adapter/vcloud/vmware_vmflavors.json");
			try {
				JsonNode node = mapper.readTree(is);
				if (node.isArray()) {
					Iterator<JsonNode> its = node.elements();
					while (its.hasNext()) {
						JsonNode typeNode = its.next();
						String name = typeNode.get("name").asText();
						String id = typeNode.get("id").asText();
						int cpu = typeNode.get("cpu").asInt();
						int memory = typeNode.get("memory").asInt();
						int disk = typeNode.get("disk").asInt();
						VmFlavor vmFlavor = new VmFlavor();
						vmFlavor.setName(name);
						vmFlavor.setId(id);
						vmFlavor.setVcpu(cpu);
						vmFlavor.setDisk(disk);
						vmFlavor.setMemory(memory);
						vmFlavors.add(vmFlavor);
					}
				}

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return vmFlavors;
	}

	@Override
	public VmFlavor getVmFlavor(String vmFlavorId) throws AdapterException {
		if (vmFlavorId == null || "".equals(vmFlavorId)) {
			throw new AdapterException("parameter vmFlavorId can not null.");
		}
		if (!vmFlavors.isEmpty()) {
			for (VmFlavor f : vmFlavors) {
				if (vmFlavorId.equals(f.getId())) {
					return f;
				}
			}
		}
		return null;
	}

}
