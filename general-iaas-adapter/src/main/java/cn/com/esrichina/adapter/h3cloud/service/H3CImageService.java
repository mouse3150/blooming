package cn.com.esrichina.adapter.h3cloud.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.com.esrichina.adapter.h3cloud.H3CloudException;
import cn.com.esrichina.adapter.h3cloud.H3CloudImage;
import cn.com.esrichina.adapter.h3cloud.H3CloudMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class H3CImageService {
	private H3CloudMethod method;

	public H3CImageService(H3CloudMethod method) {
		this.method = method;
	}

	/**
	 * 查询虚拟机镜像 根据华三image查询接口定义： /image start 起始条目，缺省0； size 返回条目，缺省10；
	 * 
	 * /image?start=11&size=10 start 起始条目； size 返回条目；
	 */

	/**
	 * { "image":[ { "uuid": "d8ecc3ad-4229-451a-bc3f-a8d33eb65ef6", "name":
	 * "Ubuntu13.10-64Bit-Desktop-40G", "description": "", "type": "0", "state":
	 * "active", "format": "qcow2", "createTime": "2014-11-04 14:55:53",
	 * "minDisk": "0", "minRam": "0", "coreCount": "1" }, { "uuid":
	 * "baf0ad0c-5553-4b5a-9247-de3edd4dc55a", "name": "windows7-64-moban",
	 * "description": "", "type": "0", "state": "active", "format": "qcow2",
	 * "createTime": "2015-01-19 14:21:50", "minDisk": "0", "minRam": "0",
	 * "coreCount": "1" }] }
	 * 
	 * 
	 */

	/**
	 * 缺省查询虚拟机镜像 根据华三image查询接口定义：/image start 起始条目，缺省0； size 返回条目，缺省10；
	 * 
	 * @return List<H3CloudImage>
	 */
	public List<H3CloudImage> getH3CImages() throws H3CloudException {
		List<H3CloudImage> images = new ArrayList<H3CloudImage>();

		try {
			String json = method.get("image", null);
			if (json != null && !"".equals(json)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(json);
				JsonNode flavorNodes = root.get("image");

				if (flavorNodes.isArray()) {
					Iterator<JsonNode> es = flavorNodes.elements();

					while (es.hasNext()) {
						H3CloudImage image = toImage(es.next());
						images.add(image);
					}
				}
			}
		} catch (JsonProcessingException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		}

		return images;
	}

	/**
	 * 缺省查询虚拟机镜像 根据华三image查询接口定义：/image?start=0&size=10 start 起始条目，缺省0； size
	 * 返回条目，缺省10；
	 * 
	 * @return List<H3CloudImage>
	 */
	public List<H3CloudImage> getH3CImages(int start, int size) throws H3CloudException {
		
		List<H3CloudImage> images = new ArrayList<H3CloudImage>();

		try {
			String json = method.get("image?start=" + start + "&size=" + size, null);
			if (json != null && !"".equals(json)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(json);
				JsonNode flavorNodes = root.get("image");

				if (flavorNodes.isArray()) {
					Iterator<JsonNode> es = flavorNodes.elements();

					while (es.hasNext()) {
						H3CloudImage image = toImage(es.next());
						images.add(image);
					}
				}
			}
		} catch (JsonProcessingException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		}

		return images;
	}

	/**
	 * 根据imageId获取Image
	 * 
	 * @param vmId
	 * @return H3CloudImage
	 */
	public H3CloudImage getH3CImageById(String imageUuid) throws H3CloudException {
		
		H3CloudImage image = null;

		if(imageUuid == null || "".equals(imageUuid)) {
			throw new H3CloudException("imageUuid can not be null.");
		}
		try {
			String json = method.get("image", imageUuid);
			
			if (json != null && !"".equals(json)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode imageNode = mapper.readTree(json);
				
				image = toImage(imageNode);

			}
		} catch (JsonProcessingException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		}

		return image;
	}
	
	private H3CloudImage toImage(JsonNode imageNode) {
		H3CloudImage image = null;
		if(imageNode != null) {
			image = new H3CloudImage();
			
			image.setUuid(imageNode.get("uuid").asText());
			image.setName(imageNode.get("name").asText());
			
			image.setDescription(imageNode.get("description").asText());
			image.setType(imageNode.get("type").asInt());
			image.setState(imageNode.get("state").asText());
			
			image.setFormat(imageNode.get("format").asText());
			image.setCreateTime(imageNode.get("createTime").asText());
			
			image.setCoreCount(imageNode.get("coreCount").asInt());
			image.setMinRam(imageNode.get("minRam").asInt());
			image.setMinDisk(imageNode.get("minDisk").asInt());
			
		}
		return image;
	}
 }
