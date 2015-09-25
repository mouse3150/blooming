/**
 * Copyright (c) 2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.08.05
 */
package cn.com.esrichina.adapter.aliyun.online;

import java.util.Properties;

import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.IaaSServiceBuilder;

public class AliyunServiceBuilder extends IaaSServiceBuilder {
	private static final String IAAS_PLATFORM = "aliyun";
	
	public AliyunServiceBuilder(Properties properties) {
		super(properties);
	}

	@Override
	public IaaSService createIaaSService() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
