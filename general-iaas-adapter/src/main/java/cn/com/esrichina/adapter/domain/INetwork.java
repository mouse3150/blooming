package cn.com.esrichina.adapter.domain;

import java.util.List;

import cn.com.esrichina.adapter.AdapterException;

public interface INetwork {
	/**
	 * Get network name
	 * @return
	 * @throws AdapterException
	 */
	public String getName() throws AdapterException;
	
	/**
	 * Get network id
	 * @return
	 * @throws AdapterException
	 */
	public String getId() throws AdapterException;
	
	/**
	 * Get All IP Addresses.
	 * @return
	 * @throws AdapterException
	 */
	public List<String> getAllIpAddresses() throws AdapterException;
	
	/**
	 * Get Allocated IP Addresses.
	 * @return
	 * @throws AdapterException
	 */
	public List<String> getAllocatedIpAddresses() throws AdapterException;
	
	/**
	 * Get Available IP Addresses.
	 * @return
	 * @throws AdapterException
	 */
	public List<String> getAvailableIpAddresses() throws AdapterException;

}
