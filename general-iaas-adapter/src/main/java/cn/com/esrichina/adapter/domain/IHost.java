package cn.com.esrichina.adapter.domain;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.HostConfiguration;
import cn.com.esrichina.adapter.feature.VirtualMachineFeature;

/**
 * 
 * @author Esri
 *
 */
public interface IHost {
	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	IDatacenter getDatacenter() throws AdapterException;

	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	String getName() throws AdapterException;
	
	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	String getHostName() throws AdapterException;
	
	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	String getHostId() throws AdapterException;

	/**
	 * 
	 * @param name
	 * @return
	 * @throws AdapterException
	 */
	IVirtualMachine getVirtualMachine(String name) throws AdapterException;

	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	IVirtualMachine[] getVirtualMachines() throws AdapterException;

	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	HostConfiguration getConfiguration() throws AdapterException;


	/**
	 * 
	 * @throws AdapterException
	 */
	void destroy() throws AdapterException;

	/**
	 * 
	 * @param vmFeature
	 * @return
	 * @throws AdapterException
	 */
	IVirtualMachine createVirtualMachine(VirtualMachineFeature vmFeature)
			throws AdapterException;

	/**
	 * 
	 * @param oldName
	 * @param newName
	 * @return
	 * @throws AdapterException
	 */
	IVirtualMachine renameVirtualMachine(String oldName, String newName)
			throws AdapterException;

	/**
	 * 
	 * @param name
	 * @throws AdapterException
	 */
	void destroyVirtualMachine(String name) throws AdapterException;

	/**
	 * 
	 * @param names
	 * @throws AdapterException
	 */
	void destroyVirtualMachines(String[] names) throws AdapterException;

}
