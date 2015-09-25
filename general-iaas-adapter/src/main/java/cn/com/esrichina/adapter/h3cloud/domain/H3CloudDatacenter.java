package cn.com.esrichina.adapter.h3cloud.domain;

import java.util.ArrayList;
import java.util.List;

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
import cn.com.esrichina.adapter.h3cloud.H3CloudImage;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod.AZone;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod.Resource;
import cn.com.esrichina.adapter.h3cloud.H3CloudNetwork;
import cn.com.esrichina.adapter.h3cloud.H3CloudServer;
import cn.com.esrichina.adapter.h3cloud.network.H3CloudNetworkImpl;
import cn.com.esrichina.adapter.h3cloud.service.H3CImageService;
import cn.com.esrichina.adapter.h3cloud.service.H3CNetworkService;
import cn.com.esrichina.adapter.h3cloud.service.H3CServerService;

public class H3CloudDatacenter implements IDatacenter {
	
	private H3CloudMethod method;
	private AZone azone;
	private H3CServerService serverService;
	private H3CImageService imageService;
	private H3CNetworkService networkService;
	
	public H3CloudDatacenter(H3CloudMethod method, AZone azone) {
		this.method = method;
		this.azone = azone;
		this.serverService = new H3CServerService(method);
		this.imageService = new H3CImageService(method);
		this.networkService = new H3CNetworkService(method);
	}

	@Override
	public String getName() throws AdapterException {
		if(azone != null) {
			return azone.name;
		}
		return null;
	}

	/**
	 * Openstack被H3Cloud之后
	 */
	
	@Override
	public String getId() throws AdapterException {
		if(azone != null) {
			return azone.name;
		}
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
	public IVirtualMachine getVirtualMachine(String vmId) throws AdapterException {
		H3CloudServer server = serverService.getH3CServerById(vmId);
		H3CloudVirtualMachine vm = null;
		if(server != null) {
			vm = new H3CloudVirtualMachine(method, server);
		}
		
		return vm;
	}

	//H3Cloud查询虚拟机，start = 0; size = 10
	@Override
	public IVirtualMachine[] getVirtualMachines() throws AdapterException {
		List<H3CloudServer> servers = serverService.getH3CServers();
		IVirtualMachine[] vms = null;
		
		if(!servers.isEmpty()) {
			vms = new IVirtualMachine[servers.size()];
			int i = 0;
			for(H3CloudServer server : servers) {
				IVirtualMachine vm = new H3CloudVirtualMachine(method, server);
				vms[i] = vm;
				i++;
			}
		}
		
		return vms;
	}

	@Override
	public IVirtualMachine[] getVirtualMachines(String site) throws AdapterException {
		//site参数只为兼容 vmware vapp结构做的处理，H3Cloud不需要处理
		List<H3CloudServer> servers = serverService.getH3CServers();
		IVirtualMachine[] vms = null;
		
		if(!servers.isEmpty()) {
			vms = new IVirtualMachine[servers.size()];
			int i = 0;
			for(H3CloudServer server : servers) {
				IVirtualMachine vm = new H3CloudVirtualMachine(method, server);
				vms[i] = vm;
				i++;
			}
		}
		
		return vms;
	}

	@Override
	public IVirtualMachine getVirtualMachine(String site, String vmName) throws AdapterException {
		H3CloudServer server = serverService.getH3CServerById(vmName);
		IVirtualMachine vm = null;
		if(server != null) {
			vm = new H3CloudVirtualMachine(method, server);
		}
		return vm;
	}

	@Override
	public IImage getImage(String imageId) throws AdapterException {
		H3CloudImage im =imageService.getH3CImageById(imageId);
		H3CloudImageImpl image = null;
		if(im != null) {
			image = new H3CloudImageImpl(method, im);
		}
		return image;
	}

	//H3Cloud分页查询 ，缺省start = 0; size=10
	//Adaptor缺省0-100条记录
	@Override
	public IImage[] getImages() throws AdapterException {
		//List<H3CloudImage> h3cImages = imageService.getH3CImages();
		List<H3CloudImage> h3cImages = imageService.getH3CImages(0, 100);
		IImage[] images = null;
		
		if(!h3cImages.isEmpty()) {
			images = new H3CloudImageImpl[h3cImages.size()];
			int i = 0;
			for(H3CloudImage im : h3cImages) {
				H3CloudImageImpl image = new H3CloudImageImpl(method, im);
				images[i] = image;
				i++;
			}
		}
		return images;
	}
	
	//Adaptor缺省0-100条记录
	@Override
	public INetwork[] getNetworks() throws AdapterException {
		//List<H3CloudNetwork> h3cNetworks = networkService.getH3CNetworks();
		List<H3CloudNetwork> h3cNetworks = networkService.getH3CNetworks(0, 100);
		INetwork[] networks = null;
		if(!h3cNetworks.isEmpty()) {
			networks = new H3CloudNetworkImpl[h3cNetworks.size()];
			int i = 0;
			
			for(H3CloudNetwork h3cNetwork : h3cNetworks) {
				INetwork network = new H3CloudNetworkImpl(method, h3cNetwork);
				networks[i] = network;
				i++;
			}
		}
		return networks;
	}

	@Override
	public INetwork getNetworkByName(String networkName) throws AdapterException {
		H3CloudNetwork h3cNetwork = networkService.getH3CNetworkById(networkName);
		H3CloudNetworkImpl network = null;
		if (h3cNetwork != null) {
			network = new H3CloudNetworkImpl(method, h3cNetwork);
		}
 		return network;
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
	public DatacenterComputeRuntimeInfo getDatacenterComputeRuntimeInfo() throws AdapterException {
		/**
		 * 0：CPU、1：内存、2：硬盘、3：IP地址段
		 */
		
		List<Resource> resources = azone.resources;
		DatacenterComputeRuntimeInfo info = null;
		if(resources != null && !resources.isEmpty()) {
			info = new DatacenterComputeRuntimeInfo();
			for(Resource res : resources) {
				if(res.resType == 0) {
					Cpu totalCpu = new Cpu(res.totalNum, CpuUnit.NUM);
					Cpu usedCpu = new Cpu(res.usedNum, CpuUnit.NUM);
					info.setTotalCpu(totalCpu);
					info.setUsedCpu(usedCpu);
				}
				
				if(res.resType == 1) {
					Memory totalMem = new Memory(res.totalNum, StorageUnit.GIGABYTES);
					Memory usedMem = new Memory(res.usedNum, StorageUnit.GIGABYTES);
					info.setTotalMemory(totalMem);
					info.setUsedMemory(usedMem);
				}
			}
		}
		info.setLimited(true);
		return info;
	}
	
	@Override
	public List<DatacenterStorageRuntimeInfo> getDatacenterStorageRuntimeInfo() throws AdapterException {
		List<DatacenterStorageRuntimeInfo> infos = new ArrayList<DatacenterStorageRuntimeInfo>();
		
		DatacenterStorageRuntimeInfo info = null;
		
		List<Resource> resources = azone.resources;
		if(resources != null && !resources.isEmpty()) {
			info = new DatacenterStorageRuntimeInfo();
			for(Resource res : resources) {
				if(res.resType == 2) {
					Storage totalStorage = new Storage(res.totalNum, StorageUnit.GIGABYTES);
					Storage usedStorage = new Storage(res.usedNum, StorageUnit.GIGABYTES);
					info.setTotal(totalStorage);
					info.setUsed(usedStorage);
				}
			}
			info.setLimited(true);
			info.setStorageName("H3C-Storage");
			infos.add(info);
		}
		
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
	public IHost addHost(HostFeature hostFeature, ClusterFeature clusterFeature) throws AdapterException {
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
	public IVirtualMachine createVirtualMachine(LaunchVmOptions launchVmOptions) throws AdapterException {
		H3CloudServer server = new H3CloudServer();
		
		if(launchVmOptions.getVmName() == null) {
			throw new AdapterException("vm's name cannot be null when creating h3cloud server.");
		}
		
		server.setName(launchVmOptions.getVmName());
		server.setDescription(launchVmOptions.getDescription());
		
		server.setImageUuid(launchVmOptions.getImageOptions().getImageId());
		
		server.setNetworkUuid(launchVmOptions.getNetworkConfigOptions().getNetworkConfigId());
		
		server.setFlavorUuid(launchVmOptions.getHardwareOptions().getId());
		
		server = serverService.create(server);
		
		H3CloudVirtualMachine vm = new H3CloudVirtualMachine(method, server);
		return vm;
	}
	
	@Override
	public IVirtualMachine virtualMachineMove(String siteSource, String vmId,
			String siteDestination) throws AdapterException {
		H3CloudServer server = serverService.getH3CServerById(vmId);
		
		H3CloudVirtualMachine vm = new H3CloudVirtualMachine(method, server);
		vm.powerOn();
		return vm;
	}

	@Override
	public IVirtualMachine createVirtualMachine(String site, LaunchVmOptions launchVmOptions) throws AdapterException {
		//site 只为兼容vmware vcloud vapp逻辑结构，h3cloud不需要处理站点。
		
		H3CloudServer server = new H3CloudServer();
		
		if(launchVmOptions.getVmName() == null) {
			throw new AdapterException("vm's name cannot be null when creating h3cloud server.");
		}
		
		server.setName(launchVmOptions.getVmName());

		server.setDescription(launchVmOptions.getDescription());
		
		server.setImageUuid(launchVmOptions.getImageOptions().getImageId());
		
		server.setNetworkUuid(launchVmOptions.getNetworkConfigOptions().getNetworkConfigId());
		
		server.setFlavorUuid(launchVmOptions.getHardwareOptions().getId());
		
		if(launchVmOptions.getNetworkConfigOptions().getPrivateIpAddress() != null
				&& !"".equals(launchVmOptions.getNetworkConfigOptions().getPrivateIpAddress())) {
			server.setPrivateIp(launchVmOptions.getNetworkConfigOptions().getPrivateIpAddress());
		}
		
		server = serverService.create(server);
		
		H3CloudVirtualMachine vm = new H3CloudVirtualMachine(method, server);
		return vm;
	}

	@Override
	public void createSite(String siteName, NetworkConfigOptions networkConfigOptions) throws AdapterException {
		//site 只为兼容vmware vcloud vapp逻辑结构，h3cloud不需要处理站点。
		//此处为空实现
	}

	@Override
	public IVirtualMachine renameVirtualMachine(String oldName, String newName) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroyVirtualMachine(String name) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyVirtualMachine(String name, VirtualMachineFeature vmFeature) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyVirtualMachines(String[] names) throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public IImage createImage(ImageFeature configuration) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IImage renameImage(String oldName, String newName) throws AdapterException {
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
