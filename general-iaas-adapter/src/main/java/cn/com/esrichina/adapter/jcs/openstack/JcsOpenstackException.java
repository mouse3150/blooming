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
