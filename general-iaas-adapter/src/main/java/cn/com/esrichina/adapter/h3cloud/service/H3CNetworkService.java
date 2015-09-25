package cn.com.esrichina.adapter.h3cloud.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.com.esrichina.adapter.h3cloud.H3CloudException;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.H3CloudNetwork;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class H3CNetworkService {
	
	private H3CloudMethod method;

	public H3CNetworkService(H3CloudMethod method) {
		this.method = method;
	}

	/**
	 * 查询网络 根据华三network查询接口定义： 
	 * /network 
	 * 		start 起始条目，缺省0；
	 * 		size 返回条目，缺省10；
	 * 
	 * /network?start=11&size=10 
	 * 		start 起始条目； 
	 * 		size 返回条目；
	 */

	/**
	 * 缺省查询查询网络 根据华三network查询接口定义：
	 * /network 
	 * 		start 起始条目，缺省0； 
	 * 		size 返回条目，缺省10；
	 * 
	 * @return List<H3CloudNetwork>
	 */
	public List<H3CloudNetwork> getH3CNetworks() throws H3CloudException {
		
		List<H3CloudNetwork> networks = new ArrayList<H3CloudNetwork>();

		try {
			String json = method.get("network", null);
			if (json != null && !"".equals(json)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(json);
				JsonNode networkNodes = root.get("network");

				if (networkNodes.isArray()) {
					Iterator<JsonNode> es = networkNodes.elements();

					while (es.hasNext()) {
						H3CloudNetwork network = toNetwork(es.next());
						networks.add(network);
					}
				} else {
					H3CloudNetwork network = toNetwork(networkNodes);
					networks.add(network);
				}
			}
		} catch (JsonProcessingException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		}

		return networks;
	}

	/**
	 * 缺省查询查询网络 根据华三network查询接口定义：
	 * 	/network?start=0&size=10 
	 * 		start 起始条目，缺省0；
	 *  	size返回条目，缺省10；
	 * 
	 * @return List<H3CloudNetwork>
	 */
	public List<H3CloudNetwork> getH3CNetworks(int start, int size) throws H3CloudException {
		
		List<H3CloudNetwork> networks = new ArrayList<H3CloudNetwork>();

		try {
			String json = method.get("network?start=" + start + "&size=" + size, null);
			if (json != null && !"".equals(json)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(json);
				JsonNode networkNodes = root.get("network");

				if (networkNodes.isArray()) {
					Iterator<JsonNode> es = networkNodes.elements();

					while (es.hasNext()) {
						H3CloudNetwork image = toNetwork(es.next());
						networks.add(image);
					}
				}
			}
		} catch (JsonProcessingException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		}

		return networks;
	}
	
	/**
	 * 根据networkId获取Network
	 * @param vmId
	 * @return H3CloudNetwork
	 */
	public H3CloudNetwork getH3CNetworkById(String networkUuid) throws H3CloudException {
		
		H3CloudNetwork network = null;

		if(networkUuid == null || "".equals(networkUuid)) {
			throw new H3CloudException("networkUuid can not be null.");
		}
		try {
			String json = method.get("network", networkUuid);
			
			if (json != null && !"".equals(json)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode networkNode = mapper.readTree(json);
				
				network = toNetwork(networkNode);

			}
		} catch (JsonProcessingException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		}

		return network;
	}
	
	private H3CloudNetwork toNetwork(JsonNode networkNode) {
		H3CloudNetwork network = null;

		if (networkNode != null) {
			network = new H3CloudNetwork();
			
			if(networkNode.get("uuid") != null) {
				network.setUuid(networkNode.get("uuid").asText());
			}
			
			if(networkNode.get("name") != null) {
				network.setName(networkNode.get("name").asText());
			}
			
			if(networkNode.get("cidr") != null) {
				network.setCidr(networkNode.get("cidr").asText());
			}
			
			if (networkNode.get("gatewayIP") != null) {
				network.setGatewayIP(networkNode.get("gatewayIP").asText());
			}
			
			if(networkNode.get("share") != null) {
				network.setShare(networkNode.get("share").asBoolean());
			}
		}

		return network;
	}
}
