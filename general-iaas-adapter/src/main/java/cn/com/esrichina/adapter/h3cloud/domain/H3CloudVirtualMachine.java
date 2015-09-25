package cn.com.esrichina.adapter.h3cloud.domain;

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
import cn.com.esrichina.adapter.h3cloud.H3CloudFlavor;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.H3CloudServer;
import cn.com.esrichina.adapter.h3cloud.service.H3CFlavorService;
import cn.com.esrichina.adapter.h3cloud.service.H3CServerService;

public class H3CloudVirtualMachine implements IVirtualMachine {
	private H3CloudServer server;
	private H3CServerService serverService;
	private H3CFlavorService flavorService;
	
	public H3CloudVirtualMachine(H3CloudMethod method, H3CloudServer server) {
		this.server = server;
		this.serverService = new H3CServerService(method);
		this.flavorService = new H3CFlavorService(method);
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
			return server.getUuid();
		}
		return null;
	}
	
	@Override
	public VmStatus getStatus() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OsInfo getOs() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VirtualMachineConfiguration getConfiguration() throws AdapterException {
		H3CloudFlavor flavor = flavorService.getH3CFlavorById(server.getFlavorUuid());
		VirtualMachineConfiguration config = new VirtualMachineConfiguration();
		ComputeResource computeRes = new ComputeResource();
		computeRes.setCpuNum(flavor.getVcpu());
		computeRes.setMemory(flavor.getMemory());
		config.setComputeResource(computeRes);
		config.setDiskSize(flavor.getDisk());
		return config;
	}

	@Override
	public VirtualMachineRuntimeInfo getRuntimeInfo() throws AdapterException {
		VirtualMachineRuntimeInfo runtimeInfo = new VirtualMachineRuntimeInfo();
		
		runtimeInfo.setHostName(server.getName());
		runtimeInfo.setPrivateIp(server.getPrivateIp());
		
		if("active".equalsIgnoreCase(server.getState())) {
			runtimeInfo.setStatus(VmStatus.RUNNING);
		} else if("stopped".equalsIgnoreCase(server.getState()) || 
				"shutoff".equalsIgnoreCase(server.getState())) {
			runtimeInfo.setStatus(VmStatus.STOPPED);
		} else if("suspended".equalsIgnoreCase(server.getState())) {
			runtimeInfo.setStatus(VmStatus.SUSPENDED);
		} else if("error".equalsIgnoreCase(server.getState())) {
			runtimeInfo.setStatus(VmStatus.ERROR);
		} else if("building".equalsIgnoreCase(server.getState())) {
			runtimeInfo.setStatus(VmStatus.PENDING);
		}
		
		return runtimeInfo;
	}

	@Override
	public INetwork[] getNetworks() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getNetworkId() throws AdapterException {
		if(server != null) {
			return server.getNetworkUuid();
		}
		return "";
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
	public void update(NetworkConfigOptions networkConfigOptions) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GuestCustomizationOptions guestCustomOptions) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() throws AdapterException {
		if(server.getUuid() == null || "".equals(server.getUuid())) {
			return;
		}
		serverService.destroy(server.getUuid());
	}

	@Override
	public void powerOn() throws AdapterException {
		if(server.getUuid() == null || "".equals(server.getUuid())) {
			return;
		}
		serverService.start(server.getUuid());

	}

	@Override
	public void powerOff() throws AdapterException {
		if(server.getUuid() == null || "".equals(server.getUuid())) {
			return;
		}
		serverService.stop(server.getUuid());

	}

	@Override
	public void reboot() throws AdapterException {
		if(server.getUuid() == null || "".equals(server.getUuid())) {
			return;
		}
		serverService.reboot(server.getUuid());

	}

	@Override
	public void suspend() throws AdapterException {

	}

}