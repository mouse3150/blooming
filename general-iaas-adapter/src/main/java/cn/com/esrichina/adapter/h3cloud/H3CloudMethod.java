package cn.com.esrichina.adapter.h3cloud;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.domain.IDatacenter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class H3CloudMethod {
	/**
	 * default SocketTimeout
	 */
	private static final int DEFAUT_SOCKET_TIMEOUT = 120000;
	/**
	 * default ConnectTimeout
	 */
	private static final int DEFAUT_CON_TIMEOUT = 120000;
	/**
	 * default ConnectionRequestTimeout
	 */
	private static final int DEFAUT_CON_REQ_TIMEOUT = 120000;

	private String endpoint;
	private String password;
	private String username;
	
	
	static public class Resource {
		/**
		 * 0：CPU、1：内存、2：硬盘、3：IP地址段
		 */
		public int resType;
		public int totalNum;
		public int usedNum;
		public String value;
	}
	
	static public class Org {
		public String  uuid;
        public String  name;
        public String ssGroupName;
        public String  description;
        public String link;
        public volatile List<Resource> resources;
        private volatile List<AZone> azones;
        public List<AZone> getAzones() {
        	return azones;
        }
        public void setAzones(List<AZone> azones) {
            this.azones = azones;
        }
		
        public List<Resource> getResources() {
			return resources;
		}
		public void setResources(List<Resource> resources) {
			this.resources = resources;
		}
    }

    static public class AZone {
    	public String uuid;
    	public String name;
        public IDatacenter dataCenter;
        public HashMap<String,String> actions;
        public List<Resource> resources;
    }
	
	public H3CloudMethod(String endpoint, String username, String password) {
		this.endpoint = endpoint;
		this.username = username;
		this.password = password;
	}
	
	public CloseableHttpClient getClient() throws AdapterException {
		CloseableHttpClient httpclient = null;
		if (endpoint == null) {
			throw new AdapterException("No cloud endpoint was defined");
		}
		boolean ssl = endpoint.startsWith("https");
		int targetPort;
		URI uri;

		try {
			uri = new URI(endpoint);
			targetPort = uri.getPort();
			if (targetPort < 1) {
				targetPort = (ssl ? 443 : 80);
			}
		} catch (URISyntaxException e) {
			throw new AdapterException(e);
		}
		HttpHost targetHost = new HttpHost(uri.getHost(), targetPort, uri.getScheme());

		RequestConfig requestConfig = RequestConfig.custom()
												.setConnectionRequestTimeout(DEFAUT_CON_REQ_TIMEOUT)
												.setConnectTimeout(DEFAUT_CON_TIMEOUT)
												.setSocketTimeout(DEFAUT_SOCKET_TIMEOUT)
												.build();

		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()), 
										new UsernamePasswordCredentials(username, password));

		if (ssl) {
			try {
				SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
					// 信任所有
					public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						return true;
					}
				}).build();
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
				httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(requestConfig)
						.setDefaultCredentialsProvider(credsProvider)
						.build();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			}
		} else {
			httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
					.setDefaultCredentialsProvider(credsProvider)
					.build();
		}

		return httpclient;

	}
	
	public List<Org> getOrgs() throws H3CloudException {
		String uri = "/organization";
		List<Org> orgs = null;
		HttpGet getOrg = new HttpGet(endpoint + uri);
		getOrg.addHeader("Accept", "application/json");
		try {
			HttpResponse resp = getClient().execute(getOrg);
			
			StatusLine status = resp.getStatusLine();
			
			if(status.getStatusCode() == HttpServletResponse.SC_OK) {
				HttpEntity entity = resp.getEntity();
				
//				for(Header h : resp.getAllHeaders()) {
//					System.out.println(h.getName() + ":" + h.getValue());
//				}
				String xml = EntityUtils.toString(entity, "UTF-8");
				orgs = loadOrgForJson(xml);
				
			} else {
				throw new H3CloudException(status.getStatusCode(), 
						EntityUtils.toString(resp.getEntity()));
			}
			
		} catch (ClientProtocolException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		} catch (AdapterException e) {
			throw new H3CloudException(e);
		} 
		return orgs;
	}
	
	private List<Org> loadOrgForJson(String json) {
		List<Org> orgs = new ArrayList<Org>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode root = mapper.readTree(json);
			JsonNode orgNode = root.get("organization");
			if(orgNode.isArray()) {
				Iterator<JsonNode> es = orgNode.elements();
				while(es.hasNext()) {
					
					Org org = toOrg(es.next()); 
					orgs.add(org);
				}
			} else {
				Org org = toOrg(orgNode); 
				orgs.add(org);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return orgs;
	}
	
	private Org toOrg(JsonNode orgNode) {
		Org org = null;
		if (orgNode != null) {
			org = new Org();
			List<Resource> resources = new ArrayList<Resource>();
			
			org.uuid = orgNode.get("uuid").asText();
			
			org.name = orgNode.get("name").asText();
			
			org.description = orgNode.get("description").asText();
			
			JsonNode link = orgNode.get("link");
			org.link = link.get("@href").asText();
			
			JsonNode resourceNode = orgNode.get("rsResourceList");
			Iterator<JsonNode> rsNodes = resourceNode.elements();
			
			while(rsNodes.hasNext()) {
				JsonNode rsNode = rsNodes.next();
				Resource rs = new Resource();
				rs.resType = rsNode.get("@resType").asInt();
				rs.totalNum = rsNode.get("@resTotalNum").asInt();
				rs.usedNum = rsNode.get("@resUseNum").asInt();
				
				if(rsNode.get("@resValue") != null) {
					rs.value = rsNode.get("@resValue").asText();
				}
				
				resources.add(rs);
			}
			//handle available zone
			//H3Cloud封装的组织与可用域之间是一对一的关系
			//可用域接口有问题，暂不能使用，只处理可用域名称
			if(null != orgNode.get("availabilityZone")) {
				String azname = orgNode.get("availabilityZone").asText();
				List<AZone> azones = new ArrayList<AZone>();
				AZone azone = new AZone();
				azone.name = azname;
				azone.resources = resources;
				azones.add(azone);
				org.setAzones(azones);
			}
			
		}
		
		return org;
	}
	
	@Deprecated
	private Org loadOrg(String xml) {
		Document doc = null;
		try {
			doc = parseXML(xml);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
		
		NodeList orgNodes = doc.getElementsByTagName("organization");
		
		for(int i = 0; i < orgNodes.getLength(); i++) {
			Org org = new Org();
			List<Resource> resources = new ArrayList<Resource>();
			Node node = orgNodes.item(i);
			NodeList childs =  node.getChildNodes();
			
			for(int j = 0; j < childs.getLength(); j++) {
				Node child = childs.item(j);
				
				String name = child.getNodeName().trim();
				String value = child.getTextContent().trim();
				if ("uuid".equals(name)) {
					org.uuid = value;
				}
				
				if ("name".equals(name)) {
					org.uuid = value;
				}
				
				if ("description".equals(name)) {
					org.description = value;
				}
				
				if ("ssGroupName".equals(name)) {
					org.ssGroupName = value;
				}
				
				if ("link".equals(name)) {
					if(child.hasAttributes()) {
						Node attr = child.getAttributes().getNamedItem("href");
						org.link = attr.getNodeValue();
					}
				}
				
				if ("link".equals(name)) {
					if(child.hasAttributes()) {
						Node attr = child.getAttributes().getNamedItem("href");
						org.link = attr.getNodeValue();
					}
				}
				
				if ("rsResourceList".equals(name)) {
					Resource rs = new Resource();
					if(child.hasAttributes()) {
						rs.resType = Integer.parseInt(child.getAttributes().getNamedItem("resType").getNodeValue());
						rs.totalNum = Integer.parseInt(child.getAttributes().getNamedItem("resTotalNum").getNodeValue());
						rs.resType = Integer.parseInt(child.getAttributes().getNamedItem("resUseNum").getNodeValue());
						
						if(child.getAttributes().getNamedItem("resValue") != null) {
							rs.value = child.getAttributes().getNamedItem("resValue").getNodeValue();
						}
						
					}
					resources.add(rs);
				}
				
				System.out.println(name + value);
			}
			
		}
		return null;
	}

	public Document parseXML(String xml) throws AdapterException {
		ByteArrayInputStream bas = null;
		try {
			bas = new ByteArrayInputStream(xml.getBytes());

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();

			return parser.parse(bas);
		} catch (ParserConfigurationException e) {
			throw new AdapterException(e);
		} catch (SAXException e) {
			throw new AdapterException(e);
		} catch (IOException e) {
			throw new AdapterException(e);
		} finally {
			if(bas != null) {
				try {
					bas.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public String get(String resource, String id) throws H3CloudException {
		String url = toURL(resource, id, null);
		
		CloseableHttpClient httpclient = null;
		HttpGet get = new HttpGet(url);
		String result = null;
		
		if(resource == null || "".equals(resource)) {
			throw new H3CloudException("GET Reource request cannot null.");
		}
		
		try {
			httpclient = getClient();
			get.addHeader("Accept", "application/json");
			

			HttpResponse resp = httpclient.execute(get);
			
			StatusLine status = resp.getStatusLine();
			
			if(status.getStatusCode() == HttpServletResponse.SC_OK) {
				HttpEntity entity = resp.getEntity();
				
				result = EntityUtils.toString(entity, "UTF-8");
				
			} else if(status.getStatusCode() == HttpServletResponse.SC_NO_CONTENT) {
				return "";
			} else {
				throw new H3CloudException(status.getStatusCode(), 
						EntityUtils.toString(resp.getEntity(), "UTF-8"));
			}
		} catch (ClientProtocolException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		} catch (AdapterException e) {
			throw new H3CloudException(e);
		} finally {
			if(httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public String put(String resource, String id, String action) throws H3CloudException {
		
		if(resource == null || "".equals(resource)) {
			throw new H3CloudException("GET Reource request cannot null.");
		}
		
		String url = toURL(resource, id, action);
		CloseableHttpClient httpclient = null;
		HttpPut put = new HttpPut(url);
		String result = null;
		
		try {
			httpclient = getClient();
			//put.addHeader("Accept", "application/json");
			
			HttpResponse resp = httpclient.execute(put);
			
			StatusLine status = resp.getStatusLine();
			
			if(status.getStatusCode() == HttpServletResponse.SC_OK) {
				HttpEntity entity = resp.getEntity();
				
				result = EntityUtils.toString(entity, "UTF-8");
				
			} else if(status.getStatusCode() == HttpServletResponse.SC_NO_CONTENT) {
				return "Success";
			} else {
				throw new H3CloudException(status.getStatusCode(), 
						EntityUtils.toString(resp.getEntity(), "UTF-8"));
			}
		} catch (ClientProtocolException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		} catch (AdapterException e) {
			throw new H3CloudException(e);
		} finally {
			if(httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	
	public String delete(String resource, String id) throws H3CloudException {
		
		if(resource == null || "".equals(resource)) {
			throw new H3CloudException("GET Reource request cannot null.");
		}
		String url = toURL(resource, id, null);
		CloseableHttpClient httpclient = null;
		HttpDelete delete = new HttpDelete(url);
		String result = null;
		
		try {
			httpclient = getClient();
			//put.addHeader("Accept", "application/json");
			
			HttpResponse resp = httpclient.execute(delete);
			
			StatusLine status = resp.getStatusLine();
			
			if(status.getStatusCode() == HttpServletResponse.SC_OK) {
				HttpEntity entity = resp.getEntity();
				
				result = EntityUtils.toString(entity, "UTF-8");
				
			} else if(status.getStatusCode() == HttpServletResponse.SC_NO_CONTENT) {
				return "Success";
			} else {
				throw new H3CloudException(status.getStatusCode(), 
						EntityUtils.toString(resp.getEntity(), "UTF-8"));
			}
		} catch (ClientProtocolException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		} catch (AdapterException e) {
			throw new H3CloudException(e);
		} finally {
			if(httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	

	public String post(String resource, String id, String payload, String contentType, String acceptType) throws H3CloudException {
		
		if(resource == null || "".equals(resource)) {
			throw new H3CloudException("GET Reource request cannot null.");
		}
		String url = toURL(resource, id, null);
		CloseableHttpClient httpclient = null;
		HttpPost post = new HttpPost(url);
		String result = null;
		
		try {
			httpclient = getClient();

			if(contentType == null) {
				contentType = "application/xml";
			}
			
			if(acceptType == null) {
				acceptType = "application/xml";
			}
			
			post.addHeader("Content-Type", contentType);
			post.addHeader("Accept", acceptType);
			
			if(payload != null) {
				StringEntity entity = new StringEntity(payload, "UTF-8");
				post.setEntity(entity);
			}
			
			HttpResponse resp = httpclient.execute(post);
			
			StatusLine status = resp.getStatusLine();
			
			if(status.getStatusCode() == HttpServletResponse.SC_OK) {
				HttpEntity entity = resp.getEntity();
				
				result = EntityUtils.toString(entity, "UTF-8");
				
			} else if(status.getStatusCode() == HttpServletResponse.SC_CREATED) {
				return "Success";
			} else {
				throw new H3CloudException(status.getStatusCode(), 
						EntityUtils.toString(resp.getEntity(), "UTF-8"));
			}
		} catch (ClientProtocolException e) {
			throw new H3CloudException(e);
		} catch (IOException e) {
			throw new H3CloudException(e);
		} catch (AdapterException e) {
			throw new H3CloudException(e);
		} finally {
			if(httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public String toURL(String resource, String id, String action) {
		StringBuffer sb = new StringBuffer(endpoint);
		if(id == null) {
			if(sb.toString().endsWith("/")) {
				sb.append(resource);
			} else {
				sb.append("/" + resource);
			}
			
		} else {
			if(sb.toString().endsWith("/")) {
				sb.append(resource);
			} else {
				sb.append("/" + resource).append("/").append(id);
			}
		}
		
		if(action != null) {
			sb.append("/").append(action);
		}
		
		return sb.toString();
	}

}
