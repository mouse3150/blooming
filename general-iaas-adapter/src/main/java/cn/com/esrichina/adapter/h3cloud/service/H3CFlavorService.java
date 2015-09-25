package cn.com.esrichina.adapter.h3cloud.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.com.esrichina.adapter.h3cloud.H3CloudException;
import cn.com.esrichina.adapter.h3cloud.H3CloudFlavor;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class H3CFlavorService {
	private H3CloudMethod method;

	public H3CFlavorService(H3CloudMethod method) {
		this.method = method;
	}

	/**
	 * 查询虚拟机配置 根据华三image查询接口定义： /flavor start 起始条目，缺省0； size 返回条目，缺省10；
	 * 
	 * /flavor?start=11&size=10 start 起始条目； size 返回条目；
	 */

	/**
	 * flavor列表
	 * { "flavor":[ { "uuid": "ff98f5c5-1346-48f5-9bf4-10bb96fa12cc", "name":
	 * "h3c.selfcustom_8x32768x40", "vcpu": "8", "memory": "32", "disk": "40" },
	 * { "uuid": "ff4b2632-ffde-4fe1-98c1-8baf0793cbd3", "name":
	 * "h3c.selfcustom_4x6144x40", "vcpu": "4", "memory": "6", "disk": "40" }] }
	 */

	/**
	 * 缺省查询虚拟机配置 根据华三flavor查询接口定义：/flavor start 起始条目，缺省0； size 返回条目，缺省10；
	 * 
	 * @return List<H3CloudFlavor>
	 */
	public List<H3CloudFlavor> getH3CFlavors() throws H3CloudException {

		List<H3CloudFlavor> flavors = new ArrayList<H3CloudFlavor>();

		try {
			String json = method.get("flavor", null);
			if (json != null && !"".equals(json)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(json);
				JsonNode flavorNodes = root.get("flavor");

				if (flavorNodes.isArray()) {
					Iterator<JsonNode> es = flavorNodes.elements();

					while (es.hasNext()) {
						H3CloudFlavor flavor = toFlavor(es.next());
						flavors.add(flavor);
					}
				}
			}
		} catch (JsonProcessingException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		}

		return flavors;
	}

	/**
	 * 缺省查询虚拟机配置 根据华三flavor查询接口定义：/flavor?start=0&size=10 start 起始条目，缺省0； size
	 * 返回条目，缺省10；
	 * 
	 * @return List<H3CloudFlavor>
	 */
	public List<H3CloudFlavor> getH3CFlavors(int start, int size) throws H3CloudException {
		List<H3CloudFlavor> flavors = new ArrayList<H3CloudFlavor>();

		try {
			String json = method.get("flavor?start=" + start + "&size=" + size, null);
			if (json != null && !"".equals(json)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(json);
				JsonNode flavorNodes = root.get("flavor");

				if (flavorNodes.isArray()) {
					Iterator<JsonNode> es = flavorNodes.elements();

					while (es.hasNext()) {
						H3CloudFlavor flavor = toFlavor(es.next());
						flavors.add(flavor);
					}
				}
			}
		} catch (JsonProcessingException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		}

		return flavors;
	}

	/**
	 * 根据flavorId获取Flavor
	 * 
	 * @param flavorUuid
	 * @return H3CloudFlavor
	 */
	public H3CloudFlavor getH3CFlavorById(String flavorUuid) throws H3CloudException {
		H3CloudFlavor flavor = null;

		if(flavorUuid == null || "".equals(flavorUuid)) {
			throw new H3CloudException("flavorUuid can not be null.");
		}
		try {
			String json = method.get("flavor", flavorUuid);
			
			if (json != null && !"".equals(json)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode flavorNode = mapper.readTree(json);
				
				flavor = toFlavor(flavorNode);

			}
		} catch (JsonProcessingException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		}

		return flavor;
	}

	/**
	 * 创建Flavor
	 * 
	 * @return H3CloudFlavor
	 * @throws H3CloudException
	 */
	public H3CloudFlavor createFlavor(H3CloudFlavor flavor) throws H3CloudException {
		return createFlavor(flavor.getVcpu(), flavor.getMemory(), flavor.getDisk());
	}

	/**
	 * 创建Flavor
	 * 
	 * @param vcpu
	 *            虚拟CPU 单位 ：个, memory， 内容大小，单位:G, disk：磁盘空间， 单位：G
	 * @return H3CloudFlavor
	 * @throws H3CloudException
	 *             @
	 */
	public H3CloudFlavor createFlavor(int vcpu, int memory, int disk) throws H3CloudException {
		
		if(vcpu < 0 || memory < 0 || disk < 0) {
			throw new H3CloudException("Invalid param when create flavor.");
		}
		
		H3CloudFlavor flavor = null;
		
		StringBuffer sb = new StringBuffer();
		sb.append("<flavor>")
					.append("<vcpu>").append(vcpu).append("</vcpu>")
					.append("<memory>").append(memory).append("</memory>")
					.append("<disk>").append(disk).append("</disk>")
		  .append("</flavor>");
		
		String flavorStr = method.post("flavor", null, sb.toString(), "application/xml", "application/json");
		
		if (flavorStr != null && !"".equals(flavorStr)) {
			ObjectMapper mapper = new ObjectMapper();
			
			try {
				JsonNode flavorNode = mapper.readTree(flavorStr);
				
				if(flavorNode != null) {
					flavor = toFlavor(flavorNode);
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return flavor;
	}

	private H3CloudFlavor toFlavor(JsonNode flavorNode) {
		H3CloudFlavor flavor = null;

		if (flavorNode != null) {
			flavor = new H3CloudFlavor();
			
			if(flavorNode.get("uuid") != null) {
				flavor.setUuid(flavorNode.get("uuid").asText());
			}
			
			if(flavorNode.get("name") != null) {
				flavor.setName(flavorNode.get("name").asText());
			}
			
			if(flavorNode.get("memory") != null) {
				flavor.setMemory(flavorNode.get("memory").asInt());
			}
			
			if(flavorNode.get("vcpu") != null) {
				flavor.setVcpu(flavorNode.get("vcpu").asInt());
			}
			
			if(flavorNode.get("disk") != null) {
				flavor.setDisk(flavorNode.get("disk").asInt());
			}
			
		}

		return flavor;
	}
}
