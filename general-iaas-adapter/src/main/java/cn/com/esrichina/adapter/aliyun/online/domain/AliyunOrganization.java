/**
 * Copyright (c) 2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.08.05
 */
package cn.com.esrichina.adapter.aliyun.online.domain;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IOrganization;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse.Region;

public class AliyunOrganization implements IOrganization {
	private IAcsClient client;
	private Region region;
	
	public AliyunOrganization(IAcsClient client, Region region) {
		this.client = client;
		this.region = region;
	}
	@Override
	public String getName() throws AdapterException {
		if(region != null) {
			region.getLocalName();
		}
		return null;
	}

	@Override
	public String getId() throws AdapterException {
		if(region != null) {
			region.getRegionId();
		}
		return null;
	}

	@Override
	public IDatacenter[] getDatacenters() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDatacenter getDatacenterByName(String name) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

}
