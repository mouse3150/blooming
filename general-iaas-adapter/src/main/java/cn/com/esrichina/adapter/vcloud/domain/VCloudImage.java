package cn.com.esrichina.adapter.vcloud.domain;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.ImageConfiguration;
import cn.com.esrichina.adapter.commons.OsInfo;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IImage;

import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.VappTemplate;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.Vdc;

public class VCloudImage implements IImage {
	
	private VcloudClient vcloudClient;
	private VappTemplate template;
	
	
	public VCloudImage() {
		
	}
	
	public VCloudImage(VcloudClient vcloudClient, VappTemplate template) {
		this.vcloudClient = vcloudClient;
		this.template = template;
	}
	
	@Override
	public String getName() throws AdapterException {
		return template.getResource().getName();
	}
	
	@Override
	public String getId() throws AdapterException {
		return template.getResource().getName();
	}
	
	@Override
	public IDatacenter getDatacenter() throws AdapterException {
		try {
			return new VCloudDatacenter(vcloudClient, Vdc.getVdcByReference(vcloudClient, template.getVdcReference()));
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@Override
	public String getState() throws AdapterException {
		return null;
	}
	
	@Override
	public long getDisk() throws AdapterException {
		try {
			return template.getVMDiskChainLength().longValue();
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public long getMemory() throws AdapterException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public OsInfo getOs() throws AdapterException {
		try {
			VappTemplate.getGuestCustomizationSection(vcloudClient, template.getReference()).getType();
		} catch (VCloudException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public IImage rename(String name) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ImageConfiguration getConfiguration() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void destroy() throws AdapterException {
		// TODO Auto-generated method stub
	}
	
	@Override
	public int getCpuHz() throws AdapterException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getCpuNum() throws AdapterException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String getDescription() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}
}
