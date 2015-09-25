package cn.com.esrichina.adapter.h3cloud.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import cn.com.esrichina.adapter.h3cloud.H3CloudException;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.H3CloudServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class H3CServerService {

	private H3CloudMethod method;
	private Logger logger = Logger.getLogger(H3CloudServer.class);

	public H3CServerService(H3CloudMethod method) {
		this.method = method;
	}

	/**
	 * 查询虚拟机 根据华三server查询接口定义： /server start 起始条目，缺省0； size 返回条目，缺省10,
	 * 最小值是10，与其他模块的查询有所不同；
	 * 
	 * /server?start=11&size=10 start 起始条目； size 返回条目；
	 */

	/**
	 * 响应结果：
	 * { "link":{ "@href":
	 * "http://10.202.2.96:8080/cloudrs/server?start=10&size=10", "@rel":
	 * "next", "@op": "GET" }, 
	 * "server":[ { "name": "WP-test1", "description":
	 * "", "imageUuid": "7ae01caa-583a-40e2-b483-c992477e0454", "networkUuid":
	 * "4d4464d8-910d-4845-812f-24f4e8f33d11", "flavorUuid":
	 * "5de3b719-3435-4e4d-923e-19a47bc28cab", "needCharge": "0", "owner": "wp",
	 * "uuid": "8f5df0c6-e993-4cb9-adf1-da51272c725f", "state": "active",
	 * "powerState": "1", "privateIp": "192.168.23.2", "createTime":
	 * "2015-03-18 10:02:31.53", "organizationId": "5", "password":
	 * "C2fbpXVCMn2b", "securityGroupList":{ "link":{ "@href":
	 * "http://10.202.2.96:8080/cloudrs/server?start=1&size=1", "@rel": "next",
	 * "@op": "GET" }, "securityGroup":{"uuid":
	 * "636b3199-1b86-4933-8691-18d11f2d5cd6", "name": "default"} }, "vncUrl":
	 * "http://10.202.2.161:6080/vnc_auto.html?token=33ab767e-07fe-4ef8-a6c4-68e0e04dcf7b"
	 * , "link":{ "@href":
	 * "http://10.202.2.96:8080/cloudrs/server/8f5df0c6-e993-4cb9-adf1-da51272c725f"
	 * , "@rel": "self", "@op": "GET" } }
	 * ] }
	 */
	/**
	 * 缺省查询虚拟机 根据华三server查询接口定义： /server start 起始条目，缺省0； size
	 * 返回条目，缺省10，最小值是10，与其他模块的查询有所不同；
	 * 
	 * @return List<H3CloudServer>
	 * @throws H3CloudException 
	 */
	public List<H3CloudServer> getH3CServers() throws H3CloudException {
		List<H3CloudServer> servers = new ArrayList<H3CloudServer>();

		try {
			String json = method.get("server", null);
			if (json != null && !"".equals(json)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(json);
				JsonNode serverNodes = root.get("server");
				
				if(serverNodes.isArray()) {
					Iterator<JsonNode> es = serverNodes.elements();
					
					while(es.hasNext()) {
						H3CloudServer server = toServer(es.next());
						servers.add(server);
					}
				}
			}
		} catch (JsonProcessingException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		}
		return servers;
	}

	/**
	 * 缺省查询虚拟机 根据华三server查询接口定义：
	 * /server?start=0&size=10 start 起始条目，缺省0；
	 * size返回条目，缺省10，最小值是10，与其他模块的查询有所不同；
	 * 
	 * @return List<H3CloudServer>
	 * @throws H3CloudException 
	 */
	public List<H3CloudServer> getH3CServers(int start, int size) throws H3CloudException {
		List<H3CloudServer> servers = new ArrayList<H3CloudServer>();

		try {
			String json = method.get("server?start=" + start + "&size=" + size, null);
			if (json != null && !"".equals(json)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(json);
				JsonNode serverNodes = root.get("server");
				
				if(serverNodes.isArray()) {
					Iterator<JsonNode> es = serverNodes.elements();
					
					while(es.hasNext()) {
						H3CloudServer server = toServer(es.next());
						servers.add(server);
					}
				}
			}
		} catch (H3CloudException e) {
			throw new H3CloudException(e);
		} catch (JsonProcessingException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		}
		return servers;
	}

	/**
	 * 根据vmId获取Server
	 * 
	 * @param vmId
	 * @return H3CloudServer
	 * @throws H3CloudException 
	 */
	public H3CloudServer getH3CServerById(String vmId) throws H3CloudException {
		H3CloudServer server = null;
		try {
			String json = method.get("server", vmId);
			if (json != null && !"".equals(json)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode serverNode = mapper.readTree(json);
				if(serverNode != null) {
					server = toServer(serverNode);
				}
			}
		} catch (H3CloudException e) {
			throw new H3CloudException(e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new H3CloudException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new H3CloudException(e);
		}
		return server;
	}

	/**
	 * 停止Server
	 * 
	 * @param vmId
	 * @throws H3CloudException 
	 */
	public void stop(String vmId) throws H3CloudException {
		String result = method.put("server", vmId, "shutdown");
		
		if("Success".equals(result)) {
			logger.info("Server id :'" + vmId + "' is stopped successfully.");
		}
	}

	/**
	 * 启动Server
	 * 
	 * @param vmId
	 * @throws H3CloudException 
	 */
	public void start(String vmId) throws H3CloudException {
		String result = method.put("server", vmId, "poweron");
		
		if("Success".equals(result)) {
			logger.info("Server id :'" + vmId + "' is started successfully.");
		}
	}

	/**
	 * 重启Server
	 * 
	 * @param vmId
	 * @throws H3CloudException 
	 */
	public void reboot(String vmId) throws H3CloudException {
		String result = method.put("server", vmId, "reboot");
		
		if("Success".equals(result)) {
			logger.info("Server id :'" + vmId + "' is rebooted successfully.");
		}
	}

	/**
	 * 销毁Server
	 * 
	 * @param vmId
	 * @throws H3CloudException 
	 */
	public void destroy(String vmId) throws H3CloudException {
		
		String result = method.delete("server", vmId);
		
		if("Success".equals(result)) {
			logger.info("Server id :'" + vmId + "' is deleted successfully.");
		}
	}

	/**
	 * 创建Server
	 * 
	 * @param vmId
	 */
	public H3CloudServer create(String serverName, String decription, String imageUuid, String flavorUuid, String networkUuid) throws H3CloudException {
		H3CloudServer server = null;
		
		if(serverName == null || "".equals(serverName)) {
			throw new H3CloudException("Must specify server name when create a server.");
		}
		
		if(imageUuid == null || "".equals(imageUuid)) {
			throw new H3CloudException("Provide imageUuid cannot null when create a server.");
		}
		
		if(flavorUuid == null || "".equals(flavorUuid)) {
			throw new H3CloudException("Provide flavorUuid cannot null when create a server.");
		}
		
		if(networkUuid == null || "".equals(networkUuid)) {
			throw new H3CloudException("Provide networkUuid cannot null when create a server.");
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<server>")
						.append("<name>").append(serverName).append("</name>")
						.append("<description>").append(decription).append("</description>")
						.append("<imageUuid>").append(imageUuid).append("</imageUuid>")
						.append("<networkUuid>").append(networkUuid).append("</networkUuid>")
						.append("<flavorUuid>").append(flavorUuid).append("</flavorUuid>")
		.append("</server>");
		String result = method.post("server", null, sb.toString(), "application/xml", "application/json");
		
		if (result != null && !"".equals(result)) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode serverNode;
			
			try {
				serverNode = mapper.readTree(result);
				
				if(serverNode != null) {
					server = toServer(serverNode);
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return server;
	}
	
	
	/**
	 * 创建Server
	 * 
	 * @param vmId
	 */
	public H3CloudServer create(H3CloudServer server) throws H3CloudException {
		
		if(server.getName() == null || "".equals(server.getName())) {
			throw new H3CloudException("Must specify server name when create a server.");
		}
		
		if(server.getImageUuid() == null || "".equals(server.getImageUuid())) {
			throw new H3CloudException("Provide imageUuid cannot null when create a server.");
		}
		
		if(server.getFlavorUuid() == null || "".equals(server.getFlavorUuid())) {
			throw new H3CloudException("Provide flavorUuid cannot null when create a server.");
		}
		
		if(server.getNetworkUuid() == null || "".equals(server.getNetworkUuid())) {
			throw new H3CloudException("Provide networkUuid cannot null when create a server.");
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<server>")
						.append("<name>").append(server.getName()).append("</name>")
						.append("<description>").append(server.getDescription()).append("</description>")
						.append("<imageUuid>").append(server.getImageUuid()).append("</imageUuid>")
						.append("<networkUuid>").append(server.getNetworkUuid()).append("</networkUuid>")
						.append("<flavorUuid>").append(server.getFlavorUuid()).append("</flavorUuid>");
		
		if(server.getPrivateIp() != null && !"".equals(server.getPrivateIp())) {
			sb.append("<privateIp>").append(server.getPrivateIp()).append("</privateIp>");
		}
						
		sb.append("</server>");
		
		String result = method.post("server", null, sb.toString(), "application/xml", "application/json");
		
		if (result != null && !"".equals(result)) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode serverNode;
			
			try {
				serverNode = mapper.readTree(result);
				
				if(serverNode != null) {
					server = toServer(serverNode);
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return server;
	}
	
	
	private H3CloudServer toServer(JsonNode serverNode) {
		H3CloudServer server = null;
		if(serverNode != null) {
			server = new H3CloudServer();
			server.setUuid(serverNode.get("uuid").asText());
			server.setName(serverNode.get("name").asText());
			
			if(serverNode.get("description") != null) {
				server.setDescription(serverNode.get("description").asText());
			}
			
			server.setImageUuid(serverNode.get("imageUuid").asText());
			server.setNetworkUuid(serverNode.get("networkUuid").asText());
			server.setFlavorUuid(serverNode.get("flavorUuid").asText());
			
			if(serverNode.get("owner") != null) {
				server.setOwner(serverNode.get("owner").asText());
			}
			
			if(serverNode.get("OrganizationId") != null) {
				server.setOrganizationId(serverNode.get("OrganizationId").asInt());
			}
			
			if(serverNode.get("privateIp") != null) {
				server.setPrivateIp(serverNode.get("privateIp").asText());
			}
			
			if(serverNode.get("createTime") != null) {
				server.setCreateTime(serverNode.get("createTime").asText());
			}
			
			if(serverNode.get("password") != null) {
				server.setPassword(serverNode.get("password").asText());
			}
			
			if(serverNode.get("state") != null) {
				server.setState(serverNode.get("state").asText());
			}
			
			if(serverNode.get("powerState") != null) {
				server.setPowerState(serverNode.get("powerState").asInt());
			}
			
			if(serverNode.get("vncUrl") != null) {
				server.setVncUrl(serverNode.get("vncUrl").asText());
			}
		}
		return server;
	}
	
	public static class IP {
		public String ipAddr;
		public String status;
	}
	
	
}
