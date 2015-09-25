package cn.com.esrichina.adapter.aliyun.test;

/**
 * Region Name/ID:深圳/cn-shenzhen Region Name/ID:青岛/cn-qingdao Region
 * Name/ID:北京/cn-beijing Region Name/ID:上海/cn-shanghai Region
 * Name/ID:香港/cn-hongkong Region Name/ID:杭州/cn-hangzhou Region
 * Name/ID:美国硅谷/us-west-1
 * 
 * @author Esri
 *
 */
public enum RegionEnum {
	BEJING("北京", "cn-beijing"), 
	SHENZHEN("深圳", "cn-shenzhen"), 
	QINGDAO("青岛", "cn-qingdao"), 
	SHANGHAI("上海", "cn-shanghai"), 
	HANGZHOU("杭州", "cn-hangzhou"), 
	HONGKONG("香港", "cn-hongkong"), 
	USA("美国", "us-west-1"), ;

	private RegionEnum(String localName, String regionId) {
		this.localName = localName;
		this.regionId = regionId;
	}

	private String localName;
	private String regionId;

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public static void main(String[] args) {
		System.out.println(RegionEnum.BEJING.getLocalName());
	}
}
