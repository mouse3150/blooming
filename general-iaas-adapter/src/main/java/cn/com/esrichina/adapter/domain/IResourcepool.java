package cn.com.esrichina.adapter.domain;

import cn.com.esrichina.adapter.AdapterException;

public interface IResourcepool {
	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	public String getName() throws AdapterException;

	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	public String getDatacenterId() throws AdapterException;

	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	public boolean getAvailable() throws AdapterException;
}
