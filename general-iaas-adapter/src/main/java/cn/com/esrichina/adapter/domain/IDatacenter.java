package cn.com.esrichina.adapter.domain;

import java.util.List;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.DatacenterComputeRuntimeInfo;
import cn.com.esrichina.adapter.commons.DatacenterConfiguration;
import cn.com.esrichina.adapter.commons.DatacenterStorageRuntimeInfo;
import cn.com.esrichina.adapter.commons.LaunchVmOptions;
import cn.com.esrichina.adapter.commons.NetworkConfigOptions;
import cn.com.esrichina.adapter.feature.ClusterFeature;
import cn.com.esrichina.adapter.feature.HostFeature;
import cn.com.esrichina.adapter.feature.ImageFeature;
import cn.com.esrichina.adapter.feature.VirtualMachineFeature;

/**
 * 
 * @author Esri
 *
 */
public interface IDatacenter {

	/**
	 * Get the DataCenter name
	 * 
	 * @return DataCenter name
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
	 * Get organization of the Data center.
	 * @return
	 * @throws AdapterException
	 */
	public IOrganization getOragnization() throws AdapterException;


	IHost getHost(String hostId) throws AdapterException;

	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	IHost[] getHosts() throws AdapterException;

	/**
	 * 
	 * @param vmName
	 * @return
	 * @throws AdapterException
	 */
	IVirtualMachine getVirtualMachine(String vmName) throws AdapterException;

	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	IVirtualMachine[] getVirtualMachines() throws AdapterException;
	
	/**
	 * Every Site has number of virtual machines.
	 * @return All virtual machines in the site.
	 * @throws AdapterException
	 */
	IVirtualMachine[] getVirtualMachines(String site) throws AdapterException;
	
	
	/**
	 * Every Site has number of virtual machines.
	 * @return
	 * @throws AdapterException
	 */
	IVirtualMachine getVirtualMachine(String site, String vmName) throws AdapterException;

	/**
	 * 
	 * @param imageId
	 * @return
	 * @throws AdapterException
	 */
	IImage getImage(String imageId) throws AdapterException;

	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	IImage[] getImages() throws AdapterException;

	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	INetwork[] getNetworks() throws AdapterException;
	
	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	INetwork getNetworkByName(String networkName) throws AdapterException;

	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	IDatastore[] getDatastores() throws AdapterException;

	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	DatacenterConfiguration getConfiguration() throws AdapterException;
	
	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	DatacenterComputeRuntimeInfo getDatacenterComputeRuntimeInfo() throws AdapterException;
	
	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	List<DatacenterStorageRuntimeInfo> getDatacenterStorageRuntimeInfo() throws AdapterException;

	/**
	 * 
	 * @param name
	 * @return
	 * @throws AdapterException
	 */
	IDatacenter rename(String name) throws AdapterException;

	/**
	 * 
	 * @throws AdapterException
	 */
	void destroy() throws AdapterException;
	
	/**
	 * 
	 * @param hostFeature
	 * @param clusterFeature
	 * @return
	 * @throws AdapterException
	 */
	IHost addHost(HostFeature hostFeature, ClusterFeature clusterFeature) throws AdapterException;

	/**
	 * @param name
	 * @throws AdapterException
	 */
	void destroyHost(String name) throws AdapterException;

	/**
	 * @param names
	 * @throws AdapterException
	 */
	void destroyHosts(String[] names) throws AdapterException;

	/**
	 * 
	 * @param launchVmOptions is configuration parameters of virtual machines.
	 * @return
	 * @throws AdapterException
	 */
	IVirtualMachine createVirtualMachine(LaunchVmOptions launchVmOptions) throws AdapterException;
	
	/**
	 * 
	 * @param launchVmOptions is configuration parameters of virtual machines.
	 * @return
	 * @throws AdapterException
	 */
	IVirtualMachine virtualMachineMove(String siteSource, String vmId, String siteDestination) throws AdapterException;
	
	/**
	 * @param site is the vm belongs to.
	 * @param vmFeature
	 * @return IVirtualMachine
	 * @throws AdapterException
	 */
	IVirtualMachine createVirtualMachine(String site, LaunchVmOptions launchVmOptions) throws AdapterException;
	
	/**
	 * @param create a logical group named site used to virtual machine container.
	 * @param siteName : virtual machine container
	 * @param networkConfigOptions : 
	 * @return IVirtualMachine
	 * @throws AdapterException
	 */
	void createSite(String siteName, NetworkConfigOptions networkConfigOptions) throws AdapterException;
	
	/**
	 * @param oldName
	 * @param newName
	 * @return
	 * @throws AdapterException
	 */
	IVirtualMachine renameVirtualMachine(String oldName, String newName) throws AdapterException;

	/**
	 * @param name
	 * @throws AdapterException
	 */
	void destroyVirtualMachine(String name) throws AdapterException;

	/**
	 * 
	 * @param name
	 * @param vmFeature
	 * @throws AdapterException
	 */
	void destroyVirtualMachine(String name, VirtualMachineFeature vmFeature) throws AdapterException;

	/**
	 * 
	 * @param names
	 * @throws AdapterException
	 */
	void destroyVirtualMachines(String[] names) throws AdapterException;

	/**
	 * 
	 * @param configuration
	 * @return
	 * @throws AdapterException
	 */
	IImage createImage(ImageFeature configuration) throws AdapterException;

	/**
	 * @param oldName
	 * @param newName
	 * @return
	 * @throws AdapterException
	 */
	IImage renameImage(String oldName, String newName) throws AdapterException;

	/**
	 * @param name
	 * @throws AdapterException
	 */
	void destroyImage(String name) throws AdapterException;
	
	/**
	 * @param name
	 * @throws AdapterException
	 */
	void destroySite(String siteName) throws AdapterException;

	/**
	 * 
	 * @param names
	 * @throws AdapterException
	 */
	void destroyImages(String[] names) throws AdapterException;

}
