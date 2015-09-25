package cn.com.esrichina.adapter.domain;

import cn.com.esrichina.adapter.AdapterException;

/**
 * Organization是IaaS多租户逻辑架构
 * 映射：
 * 1.vCloud->Organization
 * 2.vSphere->DataCenter
 * 3.AWS->Region
 * 4.OpenStack->Region.
 * .....
 * 
 * @author Esri
 *
 */

public interface IOrganization {
	/**
	 * Get the Organization name
	 * 
	 * @return Organization name
	 * @throws AdapterException
	 */
	String getName() throws AdapterException;
	
	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	String getId() throws AdapterException;
	
	/**
	 * 
	 * @return IDatacenter[]
	 * @throws AdapterException
	 */
	IDatacenter[] getDatacenters() throws AdapterException;
	
	/**
	 * 
	 * @return IDatacenter
	 * @throws AdapterException
	 */
	IDatacenter getDatacenterByName(String name) throws AdapterException;
}
