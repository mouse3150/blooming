package cn.com.esrichina.adapter.h3cloud.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.adapter.h3cloud.H3CloudException;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.H3CloudServer;
import cn.com.esrichina.adapter.h3cloud.service.H3CServerService;

public class ServerServiceTest {

	private  H3CloudMethod method;
	@Before
	public void setUp() throws Exception {
		method = new H3CloudMethod(H3Constants.endpoint, H3Constants.username, H3Constants.password);
	}

	@Test
	public void testGetServers() {
		H3CServerService serverService = new H3CServerService(method);
		List<H3CloudServer> servers = null;
		try {
			servers = serverService.getH3CServers();
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
		for(H3CloudServer s : servers) {
			System.out.println(s.getName());
			System.out.println(s.getUuid());
		}
	}

	@Test
	public void testGetServersForContition() {
		H3CServerService serverService = new H3CServerService(method);
		List<H3CloudServer> servers = null;
		try {
			servers = serverService.getH3CServers(9, 10);
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
		for(H3CloudServer s : servers) {
			System.out.println(s.getName());
		}
	}
	
	@Test
	public void testGetServerById() {
		String vmId= H3Constants.vm_id;
		H3CServerService serverService = new H3CServerService(method);
		H3CloudServer server = null;
		try {
			server = serverService.getH3CServerById(vmId);
		} catch (H3CloudException e) {
			e.printStackTrace();
		}
		System.out.println(server.getName());
	}
	
	@Test
	public void testStopServer() {
		String vmId= H3Constants.vm_id;
		H3CServerService serverService = new H3CServerService(method);
		
		try {
			serverService.stop(vmId);
		} catch (H3CloudException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void testStartServer() {
		String vmId= H3Constants.vm_id;
		H3CServerService serverService = new H3CServerService(method);
		
		try {
			serverService.start(vmId);
		} catch (H3CloudException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void testRebootServer() {
		String vmId= H3Constants.vm_id;
		H3CServerService serverService = new H3CServerService(method);
		
		try {
			serverService.reboot(vmId);
		} catch (H3CloudException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void testDestroyServer() {
		String vmId= H3Constants.vm_id;
		H3CServerService serverService = new H3CServerService(method);
		
		try {
			serverService.destroy(vmId);
		} catch (H3CloudException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void testCreateServer() {
		String imageUuid = H3Constants.image_id;
		String flavorUuid = H3Constants.flavor_id;
		String networkUuid = H3Constants.network_id;
		H3CServerService serverService = new H3CServerService(method);
		String serverName = "gisserver_test";
		String description = "create vm by api.";
		try {
			H3CloudServer s = serverService.create(serverName, description, imageUuid, flavorUuid, networkUuid);
			System.out.println(s.getName());
		} catch (H3CloudException e) {
			e.printStackTrace();
		}

	}
}
