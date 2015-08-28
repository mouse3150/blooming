package cn.com.esrichina.swift;

import java.util.Collection;

import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

public class EsriSwiftTest {

	public static void main(String[] args) {
//		Swift swift = new Swift();
//		swift.setPublicHost("http://123.57.38.160:8080");
//		int status = swift.authenticate("test", "", "tester", "testing").getStatus();
//		System.out.println(status);
		
//		AccountConfig config = new AccountConfig();
//        config.setAllowCaching(false);
//        config.setTenantName("test");
//        config.setTenantId("tenantid");
//        config.setUsername("tester");
//        config.setPassword("testing");
//        config.setAuthUrl("http://123.57.38.160:8080/v1");
//        
//        ClientImpl client = new ClientImpl(config);
//        
//       
//        Account account = client.authenticate();
//        account.getCount();
		
		Account account = new AccountFactory()
        .setUsername("test")
        .setPassword("testing")
        .setAuthUrl("http://123.57.38.160:8080/auth/v1.0")
        .setAuthenticationMethod(AuthenticationMethod.TEMPAUTH)
        .setTenantId("0")
        .setTenantName("tester")
        .createAccount();
		
		Collection<Container> containers = account.list();
		for(Container c : containers) {
			System.out.println(c.getCount());
		}
	}

}
