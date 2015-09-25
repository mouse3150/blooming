package cn.com.esrichina.vcloud;

import java.util.Map;

import com.vmware.vcloud.sdk.VappTemplate;
/**
 * A configuration of deploy a new vapp every time.
 * @author Esri
 *
 */
public class VappDeployConfig {
	private Map<VappTemplate,Integer> config;

	public Map<VappTemplate, Integer> getConfig() {
		return config;
	}

	public void setConfig(Map<VappTemplate, Integer> config) {
		this.config = config;
	}
	
}
