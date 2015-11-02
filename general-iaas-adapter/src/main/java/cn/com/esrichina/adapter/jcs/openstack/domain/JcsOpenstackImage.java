/*
 * Copyright (c) 2008-2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.10.25
 */
package cn.com.esrichina.adapter.jcs.openstack.domain;

import org.jclouds.openstack.nova.v2_0.domain.Image;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.ImageConfiguration;
import cn.com.esrichina.adapter.commons.OsInfo;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IImage;

public class JcsOpenstackImage implements IImage {
	private Image image;
	
	public JcsOpenstackImage(Image image) {
		this.image = image;
	}

	@Override
	public IDatacenter getDatacenter() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() throws AdapterException {
		if(image != null) {
			return image.getName();
		}
		return null;
	}

	@Override
	public String getId() throws AdapterException {
		if(image != null) {
			return image.getId();
		}
		return null;
	}

	@Override
	public String getDescription() throws AdapterException {
		if(image != null) {
			return "Openstack image.";
		}
		return null;
	}

	@Override
	public String getState() throws AdapterException {
		if(image != null) {
			return image.getStatus().name();
		}
		return null;
	}

	@Override
	public OsInfo getOs() throws AdapterException {
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
