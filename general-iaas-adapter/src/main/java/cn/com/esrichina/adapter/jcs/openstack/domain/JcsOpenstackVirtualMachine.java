package cn.com.esrichina.adapter.jcs.openstack.domain;

import org.jclouds.openstack.nova.v2_0.domain.Server;

import cn.com.esrichina.adapter.AdapterException;
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

public class JcsOpenstackVirtualMachine implements IVirtualMachine {

	private Server server;
	
	public JcsOpenstackVirtualMachine(Server server) {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VirtualMachineConfiguration getConfiguration()
			throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VirtualMachineRuntimeInfo getRuntimeInfo() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

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
		// TODO Auto-generated method stub

	}

	@Override
	public void powerOn() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void powerOff() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void reboot() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void suspend() throws AdapterException {
		// TODO Auto-generated method stub

	}

}
