/*
 * Copyright (c) 2008-2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.10.25
 */
package cn.com.esrichina.adapter.jcs.openstack.domain;

import org.jclouds.openstack.keystone.v2_0.domain.Tenant;
import org.jclouds.openstack.nova.v2_0.NovaApi;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IOrganization;

public class JcsOpenstackOrganization implements IOrganization {
	private Tenant tenant;
	private NovaApi novaApi;
	private String region;
	
	public JcsOpenstackOrganization(String region, Tenant tenant, NovaApi novaApi) {
		this.region = region;
		this.tenant = tenant;
		this.novaApi = novaApi;
	}

	@Override
	public String getName() throws AdapterException {
		return tenant.getName();
	}

	@Override
	public String getId() throws AdapterException {
		return tenant.getId();
	}

	@Override
	public IDatacenter[] getDatacenters() throws AdapterException {
		IDatacenter[] dcs = new JcsOpenstackDatacenter[1];
		JcsOpenstackDatacenter dc = new JcsOpenstackDatacenter(tenant.getName() + "-DC", tenant.getId(), region, tenant, novaApi);
		dcs[0] = dc;
		return dcs;
	}

	@Override
	public IDatacenter getDatacenterByName(String name) throws AdapterException {
		IDatacenter[] dcs = getDatacenters();
		for (int i = 0; i < dcs.length; i++) {
			if(dcs[i].getName().equals(name)) {
				return dcs[i];
			}
		}
		return null;
	}

}
