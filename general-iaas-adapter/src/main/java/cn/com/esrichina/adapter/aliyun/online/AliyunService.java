/**
 * Copyright (c) 2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.08.05
 */
package cn.com.esrichina.adapter.aliyun.online;

import java.util.List;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.aliyun.online.domain.AliyunOrganization;
import cn.com.esrichina.adapter.commons.VmFlavor;
import cn.com.esrichina.adapter.domain.IOrganization;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse.Region;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;

/**
 * 对于阿里公有云的个人账户，可以在不同的地域（Region）,及其不同区域（Zone）内申请和使用资源；
 * 因此阿里公有云Region相当于用户可用于申请、使用资源的组织（Organization）. 阿里公有云对应关系： Region ->
 * Organization Zone -> Datacenter
 *
 */
public class AliyunService implements IaaSService {

	private String cloudName = "aliyun";
	private String cloudVersion = "20141010";
	private String cloudProvider = "Alibaba.com";

	private IAcsClient client;

	public AliyunService() {
	}

	public AliyunService(IAcsClient client) {
		this.client = client;
	}

	@Override
	public void connect() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void disConnect() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isConnected() throws AdapterException {
		if (client != null) {
			return true;
		}
		return false;
	}

	@Override
	public void extendSession() throws AdapterException {
		// do not need to implement.
	}

	@Override
	public String getProviderName() throws AdapterException {
		return cloudProvider;
	}

	@Override
	public String getCloudName() throws AdapterException {
		return cloudName;
	}

	@Override
	public String getCloudVersion() throws AdapterException {
		return cloudVersion;
	}

	@Override
	public IOrganization[] getOrganizations() throws AdapterException {

		IOrganization[] orgs = null;
		DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
		DescribeRegionsResponse response = null;

		try {
			response = client.getAcsResponse(describeRegionsRequest);
			if (response != null && response.getRegions() != null
					&& response.getRegions().size() > 0) {
				orgs = new AliyunOrganization[response.getRegions().size()];
				int i = 0;
				for (Region r : response.getRegions()) {
					orgs[i] = new AliyunOrganization(client, r);
					i++;
				}
			}
		} catch (ServerException e) {
			throw new AdapterException(e);
		} catch (ClientException e) {
			throw new AdapterException(e);
		}

		return orgs;
	}

	@Override
	public IOrganization getOrganizationByName(String name)
			throws AdapterException {
		IOrganization[] orgs = getOrganizations();

		if (orgs != null) {
			for (IOrganization org : orgs) {
				if (name.equals(org.getName())) {
					return org;
				}
			}
		}

		return null;
	}

	@Override
	public IOrganization getCurrentOrganization() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VmFlavor> getVmFlavors() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VmFlavor getVmFlavor(String vmFlavorId) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

}
