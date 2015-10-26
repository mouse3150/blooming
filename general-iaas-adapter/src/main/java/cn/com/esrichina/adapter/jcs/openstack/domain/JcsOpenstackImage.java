package cn.com.esrichina.adapter.jcs.openstack.domain;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.ImageConfiguration;
import cn.com.esrichina.adapter.commons.OsInfo;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IImage;

public class JcsOpenstackImage implements IImage {

	@Override
	public IDatacenter getDatacenter() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getState() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OsInfo getOs() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCpuNum() throws AdapterException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCpuHz() throws AdapterException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getMemory() throws AdapterException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getDisk() throws AdapterException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ImageConfiguration getConfiguration() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IImage rename(String name) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() throws AdapterException {
		// TODO Auto-generated method stub

	}

}
