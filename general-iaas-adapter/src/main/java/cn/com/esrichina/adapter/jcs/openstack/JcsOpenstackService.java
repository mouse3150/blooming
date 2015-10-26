package cn.com.esrichina.adapter.jcs.openstack;

import java.util.List;

import org.jclouds.openstack.nova.v2_0.NovaApi;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.commons.VmFlavor;
import cn.com.esrichina.adapter.domain.IOrganization;

public class JcsOpenstackService implements IaaSService {
	private String cloudName = "OpenStack";
	private String cloudVersion = OpenstackVersion.KILO.name();
	private String cloudProvider = "OpenStack";
	private String region;
	private NovaApi novaApi; 
	
	public JcsOpenstackService() {
		
	}
	
	public JcsOpenstackService(String region, NovaApi novaApi) {
		this.region = region;
		this.novaApi = novaApi;
	}

	@Override
	public void connect() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void disConnect() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isConnected() throws AdapterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void extendSession() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getProviderName() throws AdapterException {
		return cloudProvider;
	}

	@Override
	public String getCloudName() throws AdapterException {
		// TODO Auto-generated method stub
		return cloudName;
	}

	@Override
	public String getCloudVersion() throws AdapterException {
		return cloudVersion;
	}

	@Override
	public IOrganization[] getOrganizations() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IOrganization getOrganizationByName(String name)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IOrganization getCurrentOrganization() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VmFlavor> getVmFlavors() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VmFlavor getVmFlavor(String vmFlavorId) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

}
