package cn.com.esrichina.adapter.domain;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.GuestCustomizationOptions;
import cn.com.esrichina.adapter.commons.HardwareOptions;
import cn.com.esrichina.adapter.commons.LaunchVmOptions;
import cn.com.esrichina.adapter.commons.NetworkConfigOptions;
import cn.com.esrichina.adapter.commons.OsInfo;
import cn.com.esrichina.adapter.commons.VirtualMachineConfiguration;
import cn.com.esrichina.adapter.commons.VirtualMachineRuntimeInfo;
import cn.com.esrichina.adapter.commons.VmStatus;

/**
 * 
 * @author Esri
 *
 */
public interface IVirtualMachine {
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
    IHost getHost() throws AdapterException;
    
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
    String getId() throws AdapterException;
    
    /**
     * 
     * @return
     * @throws AdapterException
     */
    VmStatus getStatus() throws AdapterException;
    
    /**
     * 
     * @return
     * @throws AdapterException
     */
    OsInfo getOs() throws AdapterException;
    
    /**
     * 
     * @return
     * @throws AdapterException
     */
    VirtualMachineConfiguration getConfiguration() throws AdapterException;
    
    /**
     * 
     * @return
     * @throws AdapterException
     */
    VirtualMachineRuntimeInfo getRuntimeInfo() throws AdapterException;
    
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
    String getNetworkId() throws AdapterException;
    
    /**
     * 
     * @return
     * @throws AdapterException
     */
    IDatastore[] getDatastores() throws AdapterException;
    
    /**
     * 
     * @param name
     * @return
     * @throws AdapterException
     */
    IVirtualMachine rename(String name) throws AdapterException;
    
    /**
     * 
     * @param name
     * @return
     * @throws AdapterException
     */
    void update(LaunchVmOptions lauchVmOptions) throws AdapterException;

    /**
     * 
     * @param name
     * @return
     * @throws AdapterException
     */
    void update(HardwareOptions hardwareOptions) throws AdapterException;

    /**
     * 
     * @param name
     * @return
     * @throws AdapterException
     */
    void update(NetworkConfigOptions networkConfigOptions) throws AdapterException;

    /**
     * 
     * @param name
     * @return
     * @throws AdapterException
     */
    void update(GuestCustomizationOptions guestCustomOptions) throws AdapterException;

    /**
     * 
     * @throws AdapterException
     */
    void destroy() throws AdapterException;
    
    /**
     * 
     * @throws AdapterException
     */
    void powerOn() throws AdapterException;
    
    /**
     * 
     * @throws AdapterException
     */
    void powerOff() throws AdapterException;
    
    /**
     * 
     * @throws AdapterException
     */
    void reboot() throws AdapterException;
    
    /**
     * 
     * @throws AdapterException
     */
    void suspend() throws AdapterException;
}
