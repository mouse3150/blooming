package cn.com.esrichina.adapter.aliyun.test;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;

final public class AliyunUtils {
	private static IAcsClient client;
	
	public static void setClient(IAcsClient client2) {
		client = client2;
	}
	
	public static <T extends AcsResponse> T execute(AcsRequest request) {
		T response = null;
		try {
			response = (T) client.getAcsResponse(request);
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return response;
	}
}
