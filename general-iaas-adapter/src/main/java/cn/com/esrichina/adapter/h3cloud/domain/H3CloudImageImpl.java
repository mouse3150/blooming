package cn.com.esrichina.adapter.h3cloud.domain;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.ImageConfiguration;
import cn.com.esrichina.adapter.commons.OsInfo;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IImage;
import cn.com.esrichina.adapter.h3cloud.H3CloudImage;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;

public class H3CloudImageImpl implements IImage {
	
	private H3CloudMethod method;
	private H3CloudImage image;

	
	public H3CloudImageImpl(H3CloudMethod method, H3CloudImage image) {
		this.method = method;
		this.image = image;
	}
	@Override
	public IDatacenter getDatacenter() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() throws AdapterException {
		if (image != null) {
			return image.getName();
		}
		return null;
	}

	@Override
	public String getId() throws AdapterException {
		if (image != null) {
			return image.getUuid();
		}
		return null;
	}

	@Override
	public String getDescription() throws AdapterException {
		if (image != null) {
			return image.getDescription();
		}
		return null;
	}

	@Override
	public String getState() throws AdapterException {
		if (image != null) {
			return image.getState();
		}
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
