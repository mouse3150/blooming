/*
 * Copyright (c) 2008-2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.10.25
 */
package cn.com.esrichina.adapter.jcs.openstack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jclouds.openstack.keystone.v2_0.domain.Tenant;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Image;
import org.jclouds.openstack.nova.v2_0.domain.Quota;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.domain.SimpleServerUsage;
import org.jclouds.openstack.nova.v2_0.domain.SimpleTenantUsage;
import org.jclouds.openstack.nova.v2_0.extensions.QuotaApi;
import org.jclouds.openstack.nova.v2_0.extensions.SimpleTenantUsageApi;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.openstack.nova.v2_0.options.CreateServerOptions;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.commons.Cpu;
import cn.com.esrichina.adapter.commons.CpuUnit;
import cn.com.esrichina.adapter.commons.DatacenterComputeRuntimeInfo;
import cn.com.esrichina.adapter.commons.DatacenterConfiguration;
import cn.com.esrichina.adapter.commons.DatacenterStorageRuntimeInfo;
import cn.com.esrichina.adapter.commons.LaunchVmOptions;
import cn.com.esrichina.adapter.commons.Memory;
import cn.com.esrichina.adapter.commons.NetworkConfigOptions;
import cn.com.esrichina.adapter.commons.Storage;
import cn.com.esrichina.adapter.commons.StorageUnit;
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
import com.google.common.collect.ImmutableList;

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
	public IVirtualMachine getVirtualMachineByName(String vmName)
			throws AdapterException {
		IVirtualMachine[] vms = toVirtualMachines();
		if(vms != null && vms.length > 0) {
			for (int i = 0; i < vms.length; i++) {
				if(vms[i].getName().equals(vmName)) {
					return vms[i];
				}
			}
		}
		return null;
	}
	
	@Deprecated
	@Override
	public IVirtualMachine getVirtualMachine(String vmName)
			throws AdapterException {
		IVirtualMachine[] vms = toVirtualMachines();
		if(vms != null && vms.length > 0) {
			for (int i = 0; i < vms.length; i++) {
				if(vms[i].getName().equals(vmName)) {
					return vms[i];
				}
			}
		}
		return null;
	}
	
	@Override
	public IVirtualMachine getVirtualMachineById(String vmId)
			throws AdapterException {
		return toVirtualMachineById(vmId);
	}
	

	@Override
	public IVirtualMachine[] getVirtualMachines() throws AdapterException {
		return toVirtualMachines();
	}

	@Override
	public IVirtualMachine[] getVirtualMachines(String site)
			throws AdapterException {
		return toVirtualMachines();
	}

	@Override
	public IVirtualMachine getVirtualMachine(String site, String vmName)
			throws AdapterException {
		IVirtualMachine[] vms = toVirtualMachines();
		if(vms != null && vms.length > 0) {
			for (int i = 0; i < vms.length; i++) {
				if(vms[i].getName().equals(vmName)) {
					return vms[i];
				}
			}
		}
		return null;
	}

	@Override
	public IImage getImage(String imageId) throws AdapterException {
		return toImageById(imageId);
	}

	@Override
	public IImage[] getImages() throws AdapterException {
		return toImages();
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
		
		return null;
	}

	@Override
	public DatacenterComputeRuntimeInfo getDatacenterComputeRuntimeInfo()
			throws AdapterException {
		QuotaApi quotaApi = novaApi.getQuotaApi(region).get();
		DatacenterComputeRuntimeInfo dcInfo = new DatacenterComputeRuntimeInfo();
		
        Quota quota = quotaApi.getByTenant(tenant.getId());
        
        Cpu cpu = new Cpu(quota.getCores(), CpuUnit.NUM);
        dcInfo.setTotalCpu(cpu);
        
        Memory mem = new Memory(quota.getRam(), StorageUnit.MEGABYTES);
        dcInfo.setTotalMemory(mem);
        
        SimpleTenantUsageApi simpleTenantUsageApi = novaApi.getSimpleTenantUsageApi(region).get();
        SimpleTenantUsage tenantUsage = simpleTenantUsageApi.get(tenant.getId());
        
        Set<SimpleServerUsage> serverUsages = tenantUsage.getServerUsages();
        
        double usedCpuNum = 0.0;
        double usedMemNum = 0.0;
        
        for(SimpleServerUsage usage : serverUsages) {
        	usedCpuNum += usage.getFlavorVcpus();
        	usedMemNum += usage.getFlavorMemoryMb();
        }
            	
    	Cpu usedCpu2 = new Cpu((int) usedCpuNum, CpuUnit.NUM);
    	dcInfo.setUsedCpu(usedCpu2);
    	    	
    	Memory usedMem2 = new Memory((int) usedMemNum, StorageUnit.MEGABYTES);
    	dcInfo.setUsedMemory(usedMem2);
    	
		return dcInfo;
	}
	
	public double round(double value){
	    return Math.round(value * 1000) / 1000.0;
	}

	@Override
	public List<DatacenterStorageRuntimeInfo> getDatacenterStorageRuntimeInfo()
			throws AdapterException {
		List<DatacenterStorageRuntimeInfo> infos = new ArrayList<DatacenterStorageRuntimeInfo>();
		
		QuotaApi quotaApi = novaApi.getQuotaApi(region).get();		
        Quota quota = quotaApi.getByTenant(tenant.getId());
        
        
		DatacenterStorageRuntimeInfo info = null;
		
		SimpleTenantUsageApi simpleTenantUsageApi = novaApi.getSimpleTenantUsageApi(region).get();
        SimpleTenantUsage tenantUsage = simpleTenantUsageApi.get(tenant.getId());
        
        Set<SimpleServerUsage> serverUsages = tenantUsage.getServerUsages();
        
        double usedStorageNum = 0.0;
        
        for(SimpleServerUsage usage : serverUsages) {
        	usedStorageNum += usage.getFlavorLocalGb();
        }
        
        info = new DatacenterStorageRuntimeInfo();
		Storage totalStorage = new Storage(quota.getGigabytes(), StorageUnit.GIGABYTES);
		Storage usedStorage = new Storage((int) usedStorageNum, StorageUnit.GIGABYTES);
		info.setTotal(totalStorage);
		info.setUsed(usedStorage);
		
		info.setLimited(true);
		info.setStorageName("OpenStack-Tenant-Storage");
		infos.add(info);
		
		return infos;
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
		ServerApi serverApi = novaApi.getServerApi(region);
		
		CreateServerOptions option = new CreateServerOptions();
		
		ServerCreated server = serverApi.create(launchVmOptions.getVmName(), launchVmOptions.getImageOptions().getImageId(), 
					launchVmOptions.getHardwareOptions().getId(), option);
		
		if(server != null && server.getId() != null) {
			
		}
		IVirtualMachine vm = getVirtualMachineById(server.getId());
		return vm;
	}

	//GIStack缓存过程，OpenStack VM迁移只需将已缓存的VM启动即可
	@Override
	public IVirtualMachine virtualMachineMove(String siteSource, String vmId,
			String siteDestination) throws AdapterException {
		IVirtualMachine vm = getVirtualMachineById(vmId);

		vm.powerOn();
		
		return vm;
	}
	
	//site 只为兼容vmware vcloud vapp逻辑结构，Openstack不需要处理站点。
	@Override
	public IVirtualMachine createVirtualMachine(String site,
			LaunchVmOptions launchVmOptions) throws AdapterException {
		return createVm(launchVmOptions);
	}

	@Override
	public void createSite(String siteName,
			NetworkConfigOptions networkConfigOptions) throws AdapterException {
		//site 只为兼容vmware vcloud vapp逻辑结构，openstack不需要处理站点。
		//此处为空实现
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
	
	private IVirtualMachine createVm(LaunchVmOptions launchVmOptions) throws AdapterException {
		ServerApi serverApi = novaApi.getServerApi(region);
		
		CreateServerOptions option = new CreateServerOptions();
		
		ServerCreated server = serverApi.create(launchVmOptions.getVmName(), launchVmOptions.getImageOptions().getImageId(), 
					launchVmOptions.getHardwareOptions().getId(), option);
		
		if(server == null || server.getId() == null || "".equals(server.getId())) {
			throw new AdapterException("create vm error. create response ServerCreated object error.");
		}
		
		IVirtualMachine vm = null;
		try {
			vm = getVirtualMachineById(server.getId());
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vm;
	}
	
	private IVirtualMachine[] toVirtualMachines() {
		ServerApi serverApi = novaApi.getServerApi(region);
		FluentIterable<Server> itrableServers = serverApi.listInDetail().concat();
		
		if(itrableServers == null) {
			return null;
		}
		
		ImmutableList<Server> servers = itrableServers.toList();
		
		IVirtualMachine[] vms = new JcsOpenstackVirtualMachine[servers.size()];
		int index= 0 ;
		
		for(Server srv : servers) {
			JcsOpenstackVirtualMachine vm = new JcsOpenstackVirtualMachine(region, novaApi,srv);
			vms[index] = vm;
			index++;
		}
		
		return vms;
	}
	
	private IVirtualMachine toVirtualMachineById(String vmId) {
		ServerApi serverApi = novaApi.getServerApi(region);
		
		Server server = serverApi.get(vmId);
		
		JcsOpenstackVirtualMachine vm = new JcsOpenstackVirtualMachine(region, novaApi, server);
		
		return vm;
	}
	
	private IImage[] toImages() {
		ImageApi imageApi = novaApi.getImageApi(region);
		FluentIterable<Image> itrableImages = imageApi.listInDetail().concat();
		
		if(itrableImages == null) {
			return null;
		}
		
		ImmutableList<Image> images = itrableImages.toList();
		
		IImage[] imgs = new JcsOpenstackImage[images.size()];
		int index= 0 ;
		
		for(Image img : images) {
			JcsOpenstackImage osImage = new JcsOpenstackImage(img);
			imgs[index] = osImage;
			index++;
		}
		return imgs;
	}
	
	private IImage toImageById(String imageId) {
		ImageApi imageApi = novaApi.getImageApi(region);
		Image image = imageApi.get(imageId);
		JcsOpenstackImage osImage = new JcsOpenstackImage(image);
		return osImage;
	}

}
