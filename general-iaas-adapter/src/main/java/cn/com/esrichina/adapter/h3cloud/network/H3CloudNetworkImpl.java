package cn.com.esrichina.adapter.h3cloud.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.domain.INetwork;
import cn.com.esrichina.adapter.h3cloud.H3CloudException;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;
import cn.com.esrichina.adapter.h3cloud.H3CloudNetwork;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class H3CloudNetworkImpl implements INetwork {
	
	private H3CloudMethod method;
	private H3CloudNetwork network;
	
	public H3CloudNetworkImpl(H3CloudMethod method, H3CloudNetwork network) {
		this.method = method;
		this.network = network;
	}

	@Override
	public String getName() throws AdapterException {
		if(network != null) {
			return network.getName();
		}
 		return null;
	}

	@Override
	public String getId() throws AdapterException {
		if(network != null) {
			return network.getUuid();
		}
 		return null;
	}

	@Override
	public List<String> getAllIpAddresses() throws AdapterException {
		List<String> allIps = getIpAddresses("all");
		return allIps;
	}

	@Override
	public List<String> getAllocatedIpAddresses() throws AdapterException {
		List<String> allocatedIps = getIpAddresses("allocated");
		return allocatedIps;
	}

	@Override
	public List<String> getAvailableIpAddresses() throws AdapterException {
		List<String> availableIps = getIpAddresses("unallocated");
		return availableIps;
	}
	
	/**
	 * 
	 * @param flag 标识 "unallocated"表示未分配, "allocated"已分配, "all"表示所有IP地址
	 * @return
	 * @throws H3CloudException
	 */
	private List<String> getIpAddresses(String flag) throws H3CloudException {
		
		List<String> ipList = new ArrayList<String>();
		
		String json = method.get("network/addresses", network.getUuid());
		
		if (json != null && !"".equals(json)) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			try {
				root = mapper.readTree(json);
				JsonNode ips = root.get("ipList").get("ip");

				if (ips.isArray()) {
					Iterator<JsonNode> es = ips.elements();

					while (es.hasNext()) {
						JsonNode kv = es.next();
						
						if("all".equals(flag)) {
							ipList.add(kv.get("ipAddr").asText());
						} else {
							if(flag.equals(kv.get("status").asText())) {
								ipList.add(kv.get("ipAddr").asText());
							}
						}
						
					}
				}
			} catch (JsonProcessingException e) {
				throw new H3CloudException(e);
			} catch (IOException e) {
				throw new H3CloudException(e);
			}
			
		}
		return ipList;
	}

}
