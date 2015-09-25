package cn.com.esrichina.adapter;

import java.util.List;

import cn.com.esrichina.adapter.commons.VmFlavor;
import cn.com.esrichina.adapter.domain.IOrganization;

/**
 * 
 * <p>
 * Represents a Service of a cloud provider. The cloud service API prescribes a
 * number of services which may or may not be implemented for a given cloud. In
 * addition, the cloud provider API describes the data center structure of the
 * underlying cloud through the concept of regions. Each provider must have at
 * least one region, which in turn has at least one zone or data center.
 * </p>
 * 
 * @author Esri
 */

public interface IaaSService {

	/**
	 * Called to initialize a cloud services with an operational context. The
	 * operational context includes authentication information etc.
	 * 
	 * @throws AdapterException
	 */
	void connect() throws AdapterException;

	void disConnect() throws AdapterException;

	boolean isConnected() throws AdapterException;
	
	void extendSession() throws AdapterException;
	
	String getProviderName() throws AdapterException;
	
	String getCloudName() throws AdapterException;
	
	String getCloudVersion() throws AdapterException;

	/**
	 * Organizations
	 * Cloud Managers access can get several Organizations.
	 * @return
	 * @throws AdapterException
	 */
	IOrganization[] getOrganizations() throws AdapterException;
	
	
	/**
	 * 
	 * @param name Organization's name
	 * @return
	 * @throws AdapterException
	 */
	IOrganization getOrganizationByName(String name) throws AdapterException;
	
	
	/**
	 * Organization
	 * Organization Manager access can get self.
	 * @return
	 * @throws AdapterException
	 */
	IOrganization getCurrentOrganization() throws AdapterException;
	
	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	List<VmFlavor> getVmFlavors() throws AdapterException;
	
	/**
	 * 
	 * @param vmFlavorId
	 * @return
	 * @throws AdapterException
	 */
	VmFlavor getVmFlavor(String vmFlavorId) throws AdapterException;

}
