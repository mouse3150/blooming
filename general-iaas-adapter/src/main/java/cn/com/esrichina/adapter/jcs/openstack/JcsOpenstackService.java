/*
 * Copyright (c) 2008-2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.10.25
 */
package cn.com.esrichina.adapter.jcs.openstack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jclouds.openstack.keystone.v2_0.KeystoneApi;
import org.jclouds.openstack.keystone.v2_0.domain.Tenant;
import org.jclouds.openstack.keystone.v2_0.features.TenantApi;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.v2_0.domain.PaginatedCollection;
import org.jclouds.openstack.v2_0.options.PaginationOptions;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.commons.VmFlavor;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.jcs.openstack.domain.JcsOpenstackOrganization;
import cn.com.esrichina.adapter.utils.Utils;

public class JcsOpenstackService implements IaaSService {
	private String cloudName = "OpenStack";
	private String cloudVersion = OpenstackVersion.KILO.name();
	private String cloudProvider = "OpenStack";
	private String region;
	private String tenantName;
	private NovaApi novaApi;
	private KeystoneApi keystoneApi;
	
	public JcsOpenstackService() {
		
	}
	
	public JcsOpenstackService(String region, String tenantName, NovaApi novaApi, KeystoneApi keystoneApi) {
		this.region = region;
		this.tenantName = tenantName;
		this.novaApi = novaApi;
		this.keystoneApi = keystoneApi;
		
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

	/**
	 * 分页大小：100
	 */
	@Override
	public IOrganization[] getOrganizations() throws AdapterException {
		Utils.checkNotNull(keystoneApi,
				"keystoneApi cannot be null. please check it.");
		
		TenantApi tenantApi = keystoneApi.getTenantApi().get();
		
		PaginatedCollection<Tenant> pageCollection = tenantApi.list(PaginationOptions.Builder.limit(100));
		
		Iterator<Tenant> it = pageCollection.iterator();
		int size = pageCollection.size();
		IOrganization[] orgs = new JcsOpenstackOrganization[size];
		int index = 0;
		
		while(it.hasNext()) {
			Tenant tenant = it.next();
			JcsOpenstackOrganization osos = new JcsOpenstackOrganization(region, tenant, novaApi);
			orgs[index] = osos;
			index++;
		}
		return orgs;
	}

	@Override
	public IOrganization getOrganizationByName(String name)
			throws AdapterException {
		IOrganization[] orgs = getOrganizations();
		
		for (int i = 0; i < orgs.length; i++) {
			if(orgs[i].getName().equals(name)) {
				return orgs[i];
			}
		}
		
		return null;
	}

	@Override
	public IOrganization getCurrentOrganization() throws AdapterException {
		IOrganization[] orgs = getOrganizations();
		
		for (int i = 0; i < orgs.length; i++) {
			if(orgs[i].getName().equals(tenantName)) {
				return orgs[i];
			}
		}
		
		return null;
	}

	@Override
	public List<VmFlavor> getVmFlavors() throws AdapterException {
		FlavorApi flavorApi =  novaApi.getFlavorApi(region);
		PaginatedCollection<Flavor> flavors = flavorApi.listInDetail(PaginationOptions.Builder.limit(100));
		Iterator<Flavor> its = flavors.iterator();
		
		if(its != null) {
			List<VmFlavor> osFlavors = new ArrayList<VmFlavor>();
			
			while(its.hasNext()) {
				Flavor flavor = its.next();
				
				VmFlavor vmf = new VmFlavor();
				vmf.setName(flavor.getName());
				vmf.setId(flavor.getId());
				vmf.setVcpu(flavor.getVcpus());
				vmf.setMemory(flavor.getRam());
				vmf.setDisk(flavor.getDisk());
				osFlavors.add(vmf);
			}
			
			return osFlavors;
		}
		
		return null;
	}

	@Override
	public VmFlavor getVmFlavor(String vmFlavorId) throws AdapterException {
		List<VmFlavor> flavors = getVmFlavors();
		
		if(flavors != null && flavors.size() > 0) {
			for(VmFlavor f : flavors) {
				if(f.getId().equals(vmFlavorId)) {
					return f;
				}
			}
		}
		return null;
	}

}
