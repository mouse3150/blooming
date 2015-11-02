/**
 * Copyright (c) 2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.08.05
 */
package cn.com.esrichina.adapter.aliyun.online.domain;

import java.util.List;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.DatacenterComputeRuntimeInfo;
import cn.com.esrichina.adapter.commons.DatacenterConfiguration;
import cn.com.esrichina.adapter.commons.DatacenterStorageRuntimeInfo;
import cn.com.esrichina.adapter.commons.LaunchVmOptions;
import cn.com.esrichina.adapter.commons.NetworkConfigOptions;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IDatastore;
import cn.com.esrichina.adapter.domain.IHost;
import cn.com.esrichina.adapter.domain.IImage;
import cn.com.esrichina.adapter.domain.INetwork;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.domain.IVirtualMachine;
import cn.com.esrichina.adapter.feature.ClusterFeature;
import cn.com.esrichina.adapter.feature.HostFeature;
import cn.com.esrichina.adapter.feature.ImageFeature;
import cn.com.esrichina.adapter.feature.VirtualMachineFeature;

public class AliyunDatacenter implements IDatacenter {

	@Override
	public String getName() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IOrganization getOragnization() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHost getHost(String hostId) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHost[] getHosts() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVirtualMachine getVirtualMachine(String vmName)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVirtualMachine[] getVirtualMachines() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVirtualMachine[] getVirtualMachines(String site)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVirtualMachine getVirtualMachine(String site, String vmName)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IVirtualMachine getVirtualMachineById(String vmId)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IVirtualMachine getVirtualMachineByName(String vmName)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IImage getImage(String imageId) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IImage[] getImages() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INetwork[] getNetworks() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INetwork getNetworkByName(String networkName)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDatastore[] getDatastores() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatacenterConfiguration getConfiguration() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatacenterComputeRuntimeInfo getDatacenterComputeRuntimeInfo()
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DatacenterStorageRuntimeInfo> getDatacenterStorageRuntimeInfo()
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDatacenter rename(String name) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public IHost addHost(HostFeature hostFeature, ClusterFeature clusterFeature)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroyHost(String name) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyHosts(String[] names) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public IVirtualMachine createVirtualMachine(LaunchVmOptions launchVmOptions)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVirtualMachine virtualMachineMove(String siteSource, String vmId,
			String siteDestination) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVirtualMachine createVirtualMachine(String site,
			LaunchVmOptions launchVmOptions) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createSite(String siteName,
			NetworkConfigOptions networkConfigOptions) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public IVirtualMachine renameVirtualMachine(String oldName, String newName)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroyVirtualMachine(String name) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyVirtualMachine(String name,
			VirtualMachineFeature vmFeature) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyVirtualMachines(String[] names) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public IImage createImage(ImageFeature configuration)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IImage renameImage(String oldName, String newName)
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroyImage(String name) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroySite(String siteName) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyImages(String[] names) throws AdapterException {
		// TODO Auto-generated method stub

	}

}
