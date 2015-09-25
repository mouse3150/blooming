package cn.com.esrichina.vcloud;

import java.util.ArrayList;
import java.util.List;

import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.sdk.Organization;
import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.Vdc;

public class OrgEntity {
	private String orgName;
	private Organization org;
	private List<VdcEntity> vdcs = new ArrayList<VdcEntity>();
	private VcloudClient vcloudClient;
	
	public OrgEntity() {
		
	}
	
	public OrgEntity(VcloudClient vcloudClient, Organization org) {
		this.vcloudClient = vcloudClient;
		this.org = org;
	}

	public String getOrgName() {
		if(orgName != null) {
			return orgName;
		}
		if(org != null) {
			orgName = org.getResource().getName();
		}
		return orgName;
	}
	
	
	public List<VdcEntity> listVdcs() {
		
		try {
			for(ReferenceType vdcRef : Organization
					.getOrganizationByReference(vcloudClient, org.getReference()).getVdcRefs()) {
				Vdc vdc = Vdc.getVdcByReference(vcloudClient, vdcRef);
				VdcEntity vdce = new VdcEntity(vdc.getResource().getName(), vcloudClient, vdc);
				vdcs.add(vdce);
			}
			
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return vdcs;
	}
	
	public VdcEntity getVdcByName(String vdcName) {
		VdcEntity vdce = null;
		try {
			for(ReferenceType vdcRef : Organization
					.getOrganizationByReference(vcloudClient, org.getReference()).getVdcRefs()) {
				if(vdcName.equals(vdcRef.getName())) {
					Vdc vdc = Vdc.getVdcByReference(vcloudClient, vdcRef);
					vdce = new VdcEntity(vdc.getResource().getName(), vcloudClient, vdc);
				}
				
			}
			
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return vdce;
	}
	
}
