package cn.com.esrichina.adapter.h3cloud;

import java.util.ArrayList;
import java.util.List;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.commons.VmFlavor;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod.Org;
import cn.com.esrichina.adapter.h3cloud.domain.H3CloudOrganization;
import cn.com.esrichina.adapter.h3cloud.service.H3CFlavorService;

public class H3CloudService implements IaaSService {

	private String cloudName = "H3Cloud";
	private String cloudVersion = "unknown";
	private String cloudProvider = "H3C";
	private H3CloudMethod method;
	private H3CFlavorService flavorService;
	
	public H3CloudService(H3CloudMethod method) {
		this.method = method;
		this.flavorService = new H3CFlavorService(method);
	}
	
	public H3CloudService(String endpoint, String userName, String password) {
		method = new H3CloudMethod(endpoint, userName, password);
		this.flavorService = new H3CFlavorService(method);
	}
	
	@Override
	public void connect() throws AdapterException {
		if(method == null) {
			throw new AdapterException("Connection is invalid.");
		}
		
	}
	
	@Override
	public void extendSession() throws AdapterException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void disConnect() throws AdapterException {
		
	}
	
	@Override
	public boolean isConnected() throws AdapterException {
		if(method == null) {
			return false;
		}
		return true;
	}
	
	
	@Override
	public String getCloudName() throws AdapterException {
		return cloudName;
	}
	
	@Override
	public String getProviderName() throws AdapterException {
		return cloudProvider;
	}
	
	@Override
	public String getCloudVersion() throws AdapterException {
		return cloudVersion;
	}
	
	@Override
	public IOrganization getOrganizationByName(String name) throws AdapterException {
		
		IOrganization[] orgs = getOrganizations();
		if(orgs != null) {
			for(IOrganization o : orgs) {
				if(name.equals(o.getName())) {
					return o;
				}
			}
		}
		return null;
	}
	
	@Override
	public IOrganization[] getOrganizations() throws AdapterException {
		List<Org>  orgs = method.getOrgs();
		
		IOrganization[] h3cOrgs = null;
		if(orgs != null && !orgs.isEmpty()) {
			h3cOrgs = new H3CloudOrganization[orgs.size()];
			
			int index = 0;
			for(Org org : orgs) {
				IOrganization o = new H3CloudOrganization(method, org);
				h3cOrgs[index] = o;
				index++;
			}
		}
		return h3cOrgs;
	}
	
	@Override
	public IOrganization getCurrentOrganization() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * H3Cloud 缺省查询10条记录： start=0, size=10;
	 * Adaptor 缺省查询100条记录： start=0, size=100;
	 */
	@Override
	public List<VmFlavor> getVmFlavors() throws AdapterException {
		//List<H3CloudFlavor> flavors = flavorService.getH3CFlavors();
		List<H3CloudFlavor> flavors = flavorService.getH3CFlavors(0, 100);
		List<VmFlavor> vmFlavors = null;
		if(!flavors.isEmpty()) {
			vmFlavors = new ArrayList<VmFlavor>();
			for(H3CloudFlavor f : flavors) {
				VmFlavor vmf = new VmFlavor();
				vmf.setId(f.getUuid());
				vmf.setName(f.getName());
				vmf.setVcpu(f.getVcpu());
				//H3Cloud Memory单位为 G，转换成M
				vmf.setMemory(f.getMemory() * 1024);
				vmf.setDisk(f.getDisk());
				vmFlavors.add(vmf);
			}
		}
		return vmFlavors;
	}
	
	@Override
	public VmFlavor getVmFlavor(String vmFlavorId) throws AdapterException {
		H3CloudFlavor flavor = flavorService.getH3CFlavorById(vmFlavorId);
		VmFlavor vmFlavor = null;
		if(flavor != null) {
			vmFlavor = new VmFlavor();
			vmFlavor.setId(flavor.getUuid());
			vmFlavor.setName(flavor.getName());
			vmFlavor.setVcpu(flavor.getVcpu());
			vmFlavor.setMemory(flavor.getMemory());
			vmFlavor.setDisk(flavor.getDisk());
		}
		return vmFlavor;
	}
}
