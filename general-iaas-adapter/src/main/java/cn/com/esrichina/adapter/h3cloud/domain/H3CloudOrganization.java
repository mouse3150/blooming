package cn.com.esrichina.adapter.h3cloud.domain;

import java.util.List;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod.AZone;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod.Org;

public class H3CloudOrganization implements IOrganization {

	private H3CloudMethod method;
	private Org org;
	
	public H3CloudOrganization(H3CloudMethod method, Org org) {
		this.method = method;
		this.org = org;
	}
	
	@Override
	public String getName() throws AdapterException {
		if (org != null) {
			return org.name;
		}
		return null;
	}

	@Override
	public String getId() throws AdapterException {
		if (org != null) {
			return org.uuid;
		}
		return null;
	}

	@Override
	public IDatacenter[] getDatacenters() throws AdapterException {
		List<AZone> zones = org.getAzones();
		H3CloudDatacenter[] datacenters = null;
 		
		if(zones != null && !zones.isEmpty()) {
 			datacenters = new H3CloudDatacenter[zones.size()];
 			int i = 0;
			for(AZone azone : zones) {
				H3CloudDatacenter dc = new H3CloudDatacenter(method, azone);
				datacenters[i] = dc;
				i++;
			}
		}
		
		return datacenters;
	}

	@Override
	public IDatacenter getDatacenterByName(String id) throws AdapterException {
		
		IDatacenter[] centers = getDatacenters();
		
		if(centers != null && centers.length > 0) {
			for(int i = 0; i < centers.length; i++) {
				if(id.equals(centers[i].getId())) {
					return centers[i];
				}
			}
		}
		return null;
	}

}
