package cn.com.esrichina.adapter.h3cloud;

import cn.com.esrichina.adapter.AdapterException;

@SuppressWarnings("serial")
public class H3CloudException extends AdapterException {
	
	public H3CloudException(Throwable throwable) {
		super(throwable);
	}
	
	public H3CloudException(String message) {
		super(message);
	}
	/**
	 * @param httpStateCode Http状态码
	 * @param message 异常原因
	 * 
	 */
	public H3CloudException(int httpStateCode, String message) {
		super("HTTP STATE CODE IS:" + httpStateCode 
				+ ", ERROR REASON:" +message);
	}
	
	/**
	 * @param H3Cloud 自定义错误码
	 * @param httpStateCode Http状态码
	 * @param message 异常原因
	 * 
	 */
	public H3CloudException(int httpStateCode, int exCode, String message) {
		super("HTTP STATE CODE IS:" + httpStateCode 
				+ ", ERROR CODE:" + exCode 
				+ ", ERROR REASON:" +message);
	}

}
