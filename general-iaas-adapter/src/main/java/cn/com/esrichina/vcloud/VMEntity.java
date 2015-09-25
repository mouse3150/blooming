package cn.com.esrichina.vcloud;

import com.vmware.vcloud.sdk.VM;
import com.vmware.vcloud.sdk.Vapp;
import com.vmware.vcloud.sdk.VcloudClient;

public class VMEntity {
	private VM vm;
	private Vapp vapp;
	private VcloudClient vcloudClient;
	
	public VMEntity() {
		
	}
	
	public VMEntity(VcloudClient vcloudClient, VM vm) {
		this.vcloudClient = vcloudClient;
		this.vm = vm;
	}
	
	public void update() {
		
	}
	
	public void start() {
		
	}
	
	public void stop() {
		
	}
	
	public void delete() {
		
	}
	
	
	

	public VM getVm() {
		return vm;
	}

	public void setVm(VM vm) {
		this.vm = vm;
	}

	public Vapp getVapp() {
		return vapp;
	}

	public void setVapp(Vapp vapp) {
		this.vapp = vapp;
	}

	public VcloudClient getVcloudClient() {
		return vcloudClient;
	}

	public void setVcloudClient(VcloudClient vcloudClient) {
		this.vcloudClient = vcloudClient;
	}
	
}
