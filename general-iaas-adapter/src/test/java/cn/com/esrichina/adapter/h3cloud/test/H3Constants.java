package cn.com.esrichina.adapter.h3cloud.test;

public class H3Constants {
	public static final String platform = "h3cloud";
	//杭州H3Cloud环境  ERSI云管理员
//	public static final String endpoint = "http://183.129.199.111:8000/cloudrs";
//	public static final String password = "esri";
//	public static final String username = "ERSI";
//	public static final String network_id = "";
//	public static final String flavor_id = "";
//	public static final String image_id = "";
//	public static final String vm_id = "";
	
	
	//杭州H3Cloud环境  chenhao组织管理员
//	public static final String endpoint = "http://183.129.199.111:8000/cloudrs";
//	public static final String password = "esri@123";
//	public static final String username = "chenhao";
//	public static final String org_name = "ERSI";
//	public static final String dc_name = "zone1";
//	public static final String network_id = "";
//	public static final String flavor_id = "";
//	public static final String image_id = "";
//	public static final String vm_id = "fcf31d62-5c66-41ea-938e-74d4bd446a91";
	
	//上海H3Cloud环境  jjw_admin组织管理员
	
	//public static final String endpoint = "http://218.242.110.1:8080/cloudrs";
	public static final String endpoint = "http://218.242.72.140/cloudrs";
	public static final String password = "jjw_admin";
	public static final String username = "jjw_admin";
	public static final String org_name = "上海市城乡建设和交通委员会";
	public static final String dc_name = "nova";
	//Windows image
	//public static final String IMAGE_ID = "e0c57d9b-effb-40fe-89ee-f4e4163dcf03";
	
	//Linux Image
	public static final String IMAGE_ID = "08ff7020-416b-4f6e-b4b2-916ff3e49c20";
	
	
	public static final String network_id = "cf1275e2-db97-408d-a9d6-1aa29db83ae5";
	/**
	 * 上海环境中自定义Flavor
	 * [id:'accf9794-b725-43c5-8dbf-cf92f5d2b508', name:'h3c.selfcustom_2x2048x100', vcpu:2, memory:2, disk:100]
	 */
	public static final String flavor_id = "accf9794-b725-43c5-8dbf-cf92f5d2b508";
	
	public static final String image_id = IMAGE_ID;//"9e56b857-63b7-4bfc-aaa0-cc303b6a5dcd";
	public static final String vm_id = "4266b2f1-cd3a-4d5b-8354-b14bd6ce2eb6";
	
	private H3Constants() {
		
	}
}
