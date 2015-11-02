/*
 * Copyright (c) 2008-2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.10.25
 */
package cn.com.esrichina.adapter.jcs.openstack.domain;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Address;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.openstack.v2_0.domain.Resource;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.ComputeResource;
import cn.com.esrichina.adapter.commons.GuestCustomizationOptions;
import cn.com.esrichina.adapter.commons.HardwareOptions;
import cn.com.esrichina.adapter.commons.LaunchVmOptions;
import cn.com.esrichina.adapter.commons.NetworkConfigOptions;
import cn.com.esrichina.adapter.commons.OsInfo;
import cn.com.esrichina.adapter.commons.VirtualMachineConfiguration;
import cn.com.esrichina.adapter.commons.VirtualMachineRuntimeInfo;
import cn.com.esrichina.adapter.commons.VmStatus;
import cn.com.esrichina.adapter.domain.IDatacenter;
import cn.com.esrichina.adapter.domain.IDatastore;
import cn.com.esrichina.adapter.domain.IHost;
import cn.com.esrichina.adapter.domain.INetwork;
import cn.com.esrichina.adapter.domain.IVirtualMachine;

import com.google.common.collect.Multimap;

public class JcsOpenstackVirtualMachine implements IVirtualMachine {
	private Logger logger = Logger.getLogger(JcsOpenstackVirtualMachine.class);

	private Server server;
	private NovaApi novaApi;
	private String region;
	
	public JcsOpenstackVirtualMachine(String region, NovaApi novaApi, Server server) {
		this.region = region;
		this.novaApi = novaApi;
		this.server = server;
	}
	
	@Override
	public IDatacenter getDatacenter() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHost getHost() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() throws AdapterException {
		if(server != null) {
			return server.getName();
		}
		return null;
	}

	@Override
	public String getId() throws AdapterException {
		if(server != null) {
			return server.getId();
		}
		return null;
	}

	@Override
	public VmStatus getStatus() throws AdapterException {
		VmStatus status = VmStatus.UNKNOWN;
		
		if(server != null) {
			switch(server.getStatus()) {
				case ACTIVE  : 
					status = VmStatus.RUNNING; break;
				case BUILD  : 
					status = VmStatus.PENDING; break;
				case SUSPENDED  : 
					status = VmStatus.SUSPENDED; break;
				case REBOOT  : 
					status = VmStatus.REBOOTING; break;
				case SHUTOFF  : 
					status = VmStatus.STOPPED; break;
				case ERROR  : 
					status = VmStatus.ERROR; break;
				default:
					break;
			}
		}
		return status;
	}

	@Override
	public OsInfo getOs() throws AdapterException {
		return null;
	}

	@Override
	public VirtualMachineConfiguration getConfiguration()
			throws AdapterException {
		VirtualMachineConfiguration vmConfig = new VirtualMachineConfiguration();
		
		ComputeResource computeRes = new ComputeResource();
		
		Resource rs = server.getFlavor();
		FlavorApi flavorApi = novaApi.getFlavorApi(region);
		
		Flavor flavor = flavorApi.get(rs.getId());
		//CPU个数（或VCPU）
		computeRes.setCpuNum(flavor.getVcpus());
		//CPU核数， 兼容VMware
		computeRes.setCoreNumPerCpu(1);
		computeRes.setMemory(flavor.getRam());
		
		vmConfig.setComputeResource(computeRes);
		vmConfig.setDiskSize(flavor.getDisk());
		
		return vmConfig;
	}

	@Override
	public VirtualMachineRuntimeInfo getRuntimeInfo() throws AdapterException {
		
		VirtualMachineRuntimeInfo vmRuntimeInfo = new VirtualMachineRuntimeInfo();
		VmStatus status = getStatus();
		vmRuntimeInfo.setStatus(status);
		
		String privateIp = null;
		//nova-network
		Multimap<String, Address> network = server.getAddresses();
		for(String key : network.keySet()) {
			if(privateIp != null) {
				continue;
			}
			Collection<Address> addresses = network.get(key);
			
			if(logger.isDebugEnabled()) {
				logger.debug("server :'" + server.getId() + "network name:" + key +"' address:" + addresses);
			}
			
			for(Address ad : addresses) {
				privateIp = ad.getAddr();
			}
			
		}
		vmRuntimeInfo.setPrivateIp(privateIp);
		
		return vmRuntimeInfo;
	}

	//暂使用nova-network,后期支持neutron
	@Override
	public INetwork[] getNetworks() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNetworkId() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDatastore[] getDatastores() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVirtualMachine rename(String name) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(LaunchVmOptions lauchVmOptions) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(HardwareOptions hardwareOptions) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(NetworkConfigOptions networkConfigOptions)
			throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GuestCustomizationOptions guestCustomOptions)
			throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() throws AdapterException {
		ServerApi serverApi = novaApi.getServerApi(region);
		
		String vmId =  getId();
		
		if(vmId == null) {
			logger.info("vmId is null, maybe the vm is not exist.");
			return;
		}
		boolean isSuccess = serverApi.delete(vmId);
		
		if(isSuccess) {
			logger.info("server: " + vmId + " delete successfully.");
		}
		
	}

	@Override
	public void powerOn() throws AdapterException {
		ServerApi serverApi = novaApi.getServerApi(region);
		
		String vmId =  getId();
		
		if(vmId == null) {
			logger.info("vmId is null, maybe the vm is not exist.");
			return;
		}
		serverApi.start(vmId);
		
		logger.info("server: " + vmId + " start successfully.");
	}

	@Override
	public void powerOff() throws AdapterException {
		ServerApi serverApi = novaApi.getServerApi(region);
		
		String vmId =  getId();
		
		if(vmId == null) {
			logger.info("vmId is null, maybe the vm is not exist.");
			return;
		}
		serverApi.stop(vmId);
		
		logger.info("server: " + vmId + " stop successfully.");
	}

	@Override
	public void reboot() throws AdapterException {
		ServerApi serverApi = novaApi.getServerApi(region);
		
		String vmId =  getId();
		
		if(vmId == null) {
			logger.info("vmId is null, maybe the vm is not exist.");
			return;
		}
		serverApi.start(vmId);
		
		logger.info("server: " + vmId + "reboot successfully.");

	}

	@Override
	public void suspend() throws AdapterException {
		ServerApi serverApi = novaApi.getServerApi(region);
		
		String vmId =  getId();
		
		if(vmId == null) {
			logger.info("vmId is null, maybe the vm is not exist.");
			return;
		}
		serverApi.stop(vmId);
		
		logger.info("server: " + vmId + " stop successfully.");
	}

}
