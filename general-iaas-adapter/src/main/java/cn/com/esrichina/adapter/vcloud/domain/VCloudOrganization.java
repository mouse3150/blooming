package cn.com.esrichina.adapter.vcloud.domain;

import java.util.Collection;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IOrganization;

import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.sdk.Organization;
import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.Vdc;

public class VCloudOrganization implements IOrganization {
	
	private VcloudClient vcloudClient;
	private Organization org;
	
	public VCloudOrganization() {
		
	}
	
	public VCloudOrganization(VcloudClient vcloudClient, Organization org) {
		this.vcloudClient = vcloudClient;
		this.org = org;
	}
	
	@Override
	public IDatacenter getDatacenterByName(String name) throws AdapterException {
		if(name == null) {
			throw new AdapterException("Datacenter name cannot be null.");
		}
		for(IDatacenter datac : getDatacenters()) {
			if(name.equals(datac.getName())) {
				return datac;
			}
		}
		return null;
	}
	
	@Override
	public IDatacenter[] getDatacenters() throws AdapterException {
		VCloudDatacenter[] datacenters = null;
		try {
			Collection<ReferenceType> vdcRefs = org.getVdcRefs();
			
			if(vdcRefs == null) {
				return null;
			}
			if(vdcRefs.size() > 0) {
				datacenters = new VCloudDatacenter[vdcRefs.size()];
				int i = 0;
				for(ReferenceType vdcRef : vdcRefs) {
					Vdc vdc = Vdc.getVdcByReference(vcloudClient, vdcRef);
					VCloudDatacenter datacenter = new VCloudDatacenter(vcloudClient, vdc);
					datacenters[i] = datacenter;
					i++;
				}
			}
			
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return datacenters;
	}
	
	@Override
	public String getId() throws AdapterException {
		return org.getResource().getId();
	}
	
	@Override
	public String getName() throws AdapterException {
		return org.getResource().getName();
	}
}
