package cn.com.esrichina.adapter.aliyun.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeDisksRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse.Disk;
import com.aliyuncs.ecs.model.v20140526.DescribeEipAddressesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeEipAddressesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeImagesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse.Image;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceAttributeRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceAttributeResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesResponse.InstanceType;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse.Region;
import com.aliyuncs.ecs.model.v20140526.DescribeZonesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeZonesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeZonesResponse.Zone;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class AliyunEcsTest {
	private static IAcsClient client;
	@Before
	public void setUp() throws Exception {
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", AliyunConstants.ACCESS_KEY_ID, AliyunConstants.ACCESS_KEY_SECRET);
		client = new DefaultAcsClient(profile);
	}
	
	//列出Region地区
	@Test
	public void testDescribeRegions() {
		DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
		describeRegionsRequest.setAcceptFormat(FormatType.JSON);
		AliyunUtils.setClient(client);
		
		DescribeRegionsResponse response = AliyunUtils.execute(describeRegionsRequest);
		if(response.getRegions() != null) {
			for(Region r : response.getRegions()) {
				System.out.println("Region Name/ID:" + r.getLocalName() + "/" + r.getRegionId());
			}
		}
		
	}
	
	//列出Region地区下的可用域
	@Test
	public void testDescribeZones() {
		DescribeZonesRequest describeZonesRequest = new DescribeZonesRequest();
		describeZonesRequest.setRegionId(RegionEnum.USA.getRegionId());
		AliyunUtils.setClient(client);
		DescribeZonesResponse response = AliyunUtils.execute(describeZonesRequest);
		
		if(response.getZones() != null) {
			for(Zone z : response.getZones()) {
				System.out.println("Zone name:" + z.getLocalName());
			}
		}
	}
	//列出可用虚拟机镜像
	@Test
	public void testDescribeImages() {
		DescribeImagesRequest describeImagesRequest = new DescribeImagesRequest();
		describeImagesRequest.setRegionId(RegionEnum.BEJING.getRegionId());
		//describeImagesRequest.setPageSize(40L);
		AliyunUtils.setClient(client);
		DescribeImagesResponse response = AliyunUtils.execute(describeImagesRequest);
		
		
		for(Image image : response.getImages()) {
			System.out.println(image.getImageName());
		}
	}
	
	//列出用户所有磁盘信息
	@Test
	public void testDescribeDisks() {
		DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
		describeDisksRequest.setRegionId(RegionEnum.BEJING.getRegionId());
		AliyunUtils.setClient(client);
		DescribeDisksResponse response = AliyunUtils.execute(describeDisksRequest);
		
		List<Disk> disks = response.getDisks();
		if(disks != null) {
			for(Disk d : disks) {
				System.out.println(d.getDiskName() + ":" + d.getInstanceId());
			}
		}
	}
	
	public void DescribeInstanceTypes() {
		DescribeInstanceTypesRequest describeInstanceTypesRequest = new DescribeInstanceTypesRequest();
		
		AliyunUtils.setClient(client);
		DescribeInstanceTypesResponse response = AliyunUtils.execute(describeInstanceTypesRequest);
		
		List<InstanceType> instanceTypes = response.getInstanceTypes();
		
		if(instanceTypes != null) {
			for(InstanceType type : instanceTypes) {
				System.out.println();
			}
		}
		
	}
	
	@Test
	public void testDescribeEipAddresses() {
		DescribeEipAddressesRequest describeEipAddressesRequest = new DescribeEipAddressesRequest();
		
		describeEipAddressesRequest.setRegionId(RegionEnum.BEJING.getRegionId());
		AliyunUtils.setClient(client);
		DescribeEipAddressesResponse response = AliyunUtils.execute(describeEipAddressesRequest);
		
//		List<Eip> eips = response.getEipAddresses();
//		if(eips != null) {
//			for(Eip eip : eips) {
//				System.out.println(eip.getIpAddress());
//			}
//		}
		
	}
	
	//根据虚拟机实例的ID查询该虚拟机实例的属性
	@Test
	public void testDescribeInstanceAttribute() {
		String instanceId = "i-25t7vmg24";
		DescribeInstanceAttributeRequest describeInstanceAttributeRequest = new DescribeInstanceAttributeRequest();
		describeInstanceAttributeRequest.setInstanceId(instanceId);
		
		AliyunUtils.setClient(client);
		DescribeInstanceAttributeResponse resp = AliyunUtils.execute(describeInstanceAttributeRequest);
		Assert.assertEquals("the same ", instanceId, resp.getInstanceId());		
	}
	
	Logger logger = LoggerFactory.getLogger(AliyunEcsTest.class);
	
	@Test
	public void testLog() {
		logger.debug("hello {} ma ?", "world");
		logger.info("打印中文 {} ", "world");
	}
}
