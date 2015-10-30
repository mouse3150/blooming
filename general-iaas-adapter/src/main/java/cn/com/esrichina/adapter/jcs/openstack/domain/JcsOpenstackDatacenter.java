package cn.com.esrichina.adapter.jcs.openstack.domain;

import java.util.Iterator;
import java.util.List;

import org.jclouds.openstack.keystone.v2_0.domain.Tenant;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;

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

import com.google.common.collect.FluentIterable;

public class JcsOpenstackDatacenter implements IDatacenter {
	private String name;
	private String id;
	private NovaApi novaApi;
	private String region;
	private Tenant tenant;
	public JcsOpenstackDatacenter(String name, String id, String region, Tenant tenant, NovaApi novaApi) {
		this.name = name;
		this.id = id;
		this.region = region;
		this.tenant = tenant;
		this.novaApi = novaApi;
	}

	@Override
	public String getName() throws AdapterException {
		return name;
	}

	@Override
	public String getId() throws AdapterException {
		return id;
	}

	@Override
	public IOrganization getOragnization() throws AdapterException {
		return new JcsOpenstackOrganization(region, tenant, novaApi);
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
		ServerApi serverApi = novaApi.getServerApi(region);
		
		 for (Server server : serverApi.listInDetail().concat()) {
              
         }
		return null;
	}

	@Override
	public IVirtualMachine[] getVirtualMachines() throws AdapterException {
		ServerApi serverApi = novaApi.getServerApi(region);
		FluentIterable<Server> itrableServers = serverApi.listInDetail().concat();
		
		if(itrableServers == null) {
			return null;
		}
		
		Iterator<Server> servers = itrableServers.iterator();
		IVirtualMachine[] vms = new JcsOpenstackVirtualMachine[itrableServers.size()];
		int index= 0 ;
		while(servers.hasNext()) {
			JcsOpenstackVirtualMachine vm = new JcsOpenstackVirtualMachine(servers.next());
			vms[index] = vm;
			index++;
		}
		return vms;
	}

	@Override
	public IVirtualMachine[] getVirtualMachines(String site)
			throws AdapterException {
		ServerApi serverApi = novaApi.getServerApi(region);
		FluentIterable<Server> itrableServers = serverApi.listInDetail().concat();
		
		if(itrableServers == null) {
			return null;
		}
		
		Iterator<Server> servers = itrableServers.iterator();
		IVirtualMachine[] vms = new JcsOpenstackVirtualMachine[itrableServers.size()];
		int index= 0 ;
		while(servers.hasNext()) {
			JcsOpenstackVirtualMachine vm = new JcsOpenstackVirtualMachine(servers.next());
			vms[index] = vm;
			index++;
		}
		return vms;
	}

	@Override
	public IVirtualMachine getVirtualMachine(String site, String vmName)
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
