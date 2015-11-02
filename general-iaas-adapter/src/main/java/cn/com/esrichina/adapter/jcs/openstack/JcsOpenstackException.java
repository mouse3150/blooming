/*
 * Copyright (c) 2008-2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.10.25
 */
package cn.com.esrichina.adapter.jcs.openstack;

import cn.com.esrichina.adapter.AdapterException;

@SuppressWarnings("serial")
public class JcsOpenstackException extends AdapterException {
	public JcsOpenstackException(Throwable throwable) {
		super(throwable);
	}
	
	public JcsOpenstackException(String message) {
		super(message);
	}
}
