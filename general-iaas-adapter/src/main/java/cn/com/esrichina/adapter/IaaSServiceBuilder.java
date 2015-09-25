package cn.com.esrichina.adapter;

import java.util.Properties;

public abstract class IaaSServiceBuilder {
	protected Properties properties;

	public IaaSServiceBuilder(Properties properties) {
		this.properties = properties;
	}

	public abstract IaaSService createIaaSService() throws AdapterException;
}
