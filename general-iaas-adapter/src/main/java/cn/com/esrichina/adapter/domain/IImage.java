package cn.com.esrichina.adapter.domain;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.ImageConfiguration;
import cn.com.esrichina.adapter.commons.OsInfo;

public interface IImage {
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
    String getId() throws AdapterException;
    
    /**
     * 
     * @return
     * @throws AdapterException
     */
    String getDescription() throws AdapterException;
    
    /**
     * 
     * @return
     * @throws AdapterException
     */
    String getState() throws AdapterException;
    
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
    int getCpuNum() throws AdapterException;
    
    /**
     * 
     * @return
     * @throws AdapterException
     */
    int getCpuHz() throws AdapterException;
    
    /**
     * 
     * @return
     * @throws AdapterException
     */
    long getMemory() throws AdapterException;
    
    /**
     * 
     * @return
     * @throws AdapterException
     */
    long getDisk() throws AdapterException;
    
    /**
     * 
     * @return
     * @throws AdapterException
     */
    ImageConfiguration getConfiguration() throws AdapterException;
    
    /**
     * 
     * @param name
     * @return
     * @throws AdapterException
     */
    IImage rename(String name) throws AdapterException;
    
    /**
     * 
     * @throws AdapterException
     */
    void destroy() throws AdapterException;
}
