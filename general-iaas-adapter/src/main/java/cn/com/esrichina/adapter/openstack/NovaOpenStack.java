package cn.com.esrichina.adapter.openstack;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.CloudErrorType;
import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.commons.VmFlavor;
import cn.com.esrichina.adapter.domain.IOrganization;
import cn.com.esrichina.adapter.utils.Cache;
import cn.com.esrichina.adapter.utils.CacheLevel;
import cn.com.esrichina.adapter.utils.time.Day;
import cn.com.esrichina.adapter.utils.time.TimePeriod;

public class NovaOpenStack implements IaaSService {
	
	private ProviderContext context;
    
	static private final Logger logger = getLogger(NovaOpenStack.class, "std");

    static private String getLastItem(String name) {
        int idx = name.lastIndexOf('.');
        
        if( idx < 0 ) {
            return name;
        }
        else if( idx == (name.length()-1) ) {
            return "";
        }
        return name.substring(idx + 1);
    }
    
    static public Logger getLogger(Class<?> cls, String type) {
        String pkg = getLastItem(cls.getPackage().getName());
        
        if( pkg.equals("openstack") ) {
            pkg = "";
        }
        else {
            pkg = pkg + ".";
        }
        return LoggerFactory.getLogger("cn.com.esrichina.adapter." + type + "." + pkg + getLastItem(cls.getName()));
    }

    static public boolean isSupported(String version) {
        int idx = version.indexOf('.');
        int major, minor;
        
        if( idx < 0 ) {
            major = Integer.parseInt(version);
            minor = 0;
        }
        else {
            String[] parts = version.split("\\.");
            
            major = Integer.parseInt(parts[0]);
            minor = Integer.parseInt(parts[1]);
        }
        return (major <= 2 && minor < 10);
    }
    
    public NovaOpenStack() { }
    
    public NovaOpenStack(ProviderContext context) {
    	this.context = context;
    }
    
    @SuppressWarnings("finally")
	public synchronized AuthenticationContext getAuthenticationContext() throws AdapterException {
        try {
            Cache<AuthenticationContext> cache = Cache.getInstance(this, "authenticationContext", AuthenticationContext.class, CacheLevel.REGION_ACCOUNT, new TimePeriod<Day>(1, TimePeriod.DAY));
            ProviderContext ctx = getContext();

            if( ctx == null ) {
                throw new AdapterException("No context was set for this request");
            }
            Iterable<AuthenticationContext> current = cache.get(ctx);
            AuthenticationContext authenticationContext = null;

            NovaMethod method = new NovaMethod(this);

            if( current != null ) {
                authenticationContext = current.iterator().next();
            }
            else {
                try {
                    authenticationContext = method.authenticate();
                }
                finally {
                    if( authenticationContext == null ) {
                        NovaException.ExceptionItems items = new NovaException.ExceptionItems();

                        items.code = HttpStatus.SC_UNAUTHORIZED;
                        items.type = CloudErrorType.AUTHENTICATION;
                        items.message = "unauthorized";
                        items.details = "The API keys failed to authenticate with the specified endpoint.";
                        throw new NovaException(items);
                    }
                    cache.put(ctx, Collections.singletonList(authenticationContext));
                    return authenticationContext;
                  }
            }
            return authenticationContext;
        } catch(AdapterException e) {
        	throw new AdapterException(e);
        }
    }
    
    public String getCloudName() {
        ProviderContext ctx = getContext();
        String name = (ctx == null ? null : ctx.getCloudName());

        return (name != null ? name : "OpenStack");
    }
    
    public String getProviderName() {
        ProviderContext ctx = getContext();
        String name = (ctx == null ? null : ctx.getProvider().name());
        
        return (name != null ? name : "OpenStack");
    }
    
    public int getMajorVersion() throws AdapterException {
        AuthenticationContext ctx = getAuthenticationContext();
        String endpoint = ctx.getComputeUrl();
        
        if( endpoint == null ) {
            endpoint = ctx.getStorageUrl();
            if( endpoint == null ) {
                return 1;
            }
        }
        while( endpoint.endsWith("/") && endpoint.length() > 1 ) {
            endpoint = endpoint.substring(0,endpoint.length()-1);
        }
        String[] parts = endpoint.split("/");
        int idx = parts.length-1;
        
        do {
            endpoint = parts[idx];
            while( !Character.isDigit(endpoint.charAt(0)) && endpoint.length() > 1 ) {
                endpoint = endpoint.substring(1);
            }
            if( Character.isDigit(endpoint.charAt(0)) ) {
                int i = endpoint.indexOf('.');
                
                try {
                    if( i == -1 ) {
                        return Integer.parseInt(endpoint);
                    }
                    String[] d = endpoint.split("\\.");

                    return Integer.parseInt(d[0]);
                }
                catch(NumberFormatException ignore ) {
                    // ignore
                }
            }
        } while( (idx--) > 0 );
        return 1;
    }
    
    public int getMinorVersion() throws AdapterException {
        AuthenticationContext ctx = getAuthenticationContext();
        String endpoint = ctx.getComputeUrl();
        
        if( endpoint == null ) {
            endpoint = ctx.getStorageUrl();
            if( endpoint == null ) {
                return 1;
            }
        }
        while( endpoint.endsWith("/") && endpoint.length() > 1 ) {
            endpoint = endpoint.substring(0,endpoint.length()-1);
        }
        String[] parts = endpoint.split("/");
        int idx = parts.length-1;
        
        do {
            endpoint = parts[idx];
            while( !Character.isDigit(endpoint.charAt(0)) && endpoint.length() > 1 ) {
                endpoint = endpoint.substring(1);
            }
            if( Character.isDigit(endpoint.charAt(0)) ) {
                int i = endpoint.indexOf('.');
                
                try {
                    if( i == -1 ) {
                        return Integer.parseInt(endpoint);
                    }
                    String[] d = endpoint.split("\\.");

                    return Integer.parseInt(d[1]);
                }
                catch( NumberFormatException ignore ) {
                    // ignore
                }
            }
        } while( (idx--) > 0 );
        return 1;
    }

    public boolean isInsecure() {
        ProviderContext ctx = getContext();
        String value;

        if( ctx == null ) {
            value = null;
        }
        else {
            Properties p = ctx.getCustomProperties();

            if( p == null ) {
                value = null;
            }
            else {
                value = p.getProperty("insecure");
            }
        }
        if( value == null ) {
            value = System.getProperty("insecure");
        }
        return (value != null && value.equalsIgnoreCase("true"));
    }
    
    public long parseTimestamp(String time) throws AdapterException {
        if( time == null ) {
            return 0L;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            
        if( time.length() > 0 ) {
            try {
                return fmt.parse(time).getTime();
            } 
            catch( ParseException e ) {
                fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    return fmt.parse(time).getTime();
                }
                catch( ParseException encore ) {
                    fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    try {
                        return fmt.parse(time).getTime();
                    }
                    catch( ParseException again ) {
                        try {
                            return fmt.parse(time).getTime();
                        }
                        catch( ParseException whynot ) {
                            fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                return fmt.parse(time).getTime();
                            }
                            catch( ParseException because ) {
                                throw new AdapterException("Could not parse date: " + time);
                            }
                        }
                    }
                }
            }
        }
        return 0L;
    }
    
    public String testContext() throws AdapterException {
        try {
		    AuthenticationContext ctx = getAuthenticationContext();

		    return (ctx == null ? null : ctx.getTenantId());
		}
		catch( Throwable t ) {
		    logger.warn("Failed to test OpenStack connection context: " + t.getMessage());
		    return null;
		}

    }
    
    public void createTags( String service, String resource, String resourceId, Tag... keyValuePairs ) throws AdapterException {
    	try {
			NovaMethod method = new NovaMethod(this);
			HashMap<String,Object> json = new HashMap<String, Object>();
			Map<String, Object> newMeta = new HashMap<String, Object>();
			for (int i = 0; i < keyValuePairs.length; i++) {
				newMeta.put( keyValuePairs[i].getKey().toLowerCase(), keyValuePairs[i].getValue() != null ? keyValuePairs[i].getValue() : "");
			}
			json.put("metadata", newMeta);
			method.putString(service, resource, resourceId, new JSONObject(json), "metadata");
		} catch( Exception e ) {
			logger.error("Error while creating tags for " + resource + " - " + resourceId + ".", e);
		}
    }
    
    public void updateTags( String service, String resource, String resourceId, Tag... keyValuePairs ) throws AdapterException {
    	try {
    		try {
    			NovaMethod method = new NovaMethod(this);
    			HashMap<String,Object> json = new HashMap<String, Object>();
    			Map<String, Object> newMeta = new HashMap<String, Object>();
    			for (int i = 0; i < keyValuePairs.length; i++) {
    				newMeta.put( keyValuePairs[i].getKey().toLowerCase(), keyValuePairs[i].getValue() != null ? keyValuePairs[i].getValue() : "");
    			}
    			json.put("metadata", newMeta);
    			method.postString(service, resource, resourceId, "metadata", new JSONObject(json));
    		} catch( Exception e ) {
    			logger.error("Error while updating tags for " + resource + " - " + resourceId + ".", e);
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void removeTags( String service, String resource, String resourceId, Tag... keyValuePairs ) throws AdapterException {
    	try {
			NovaMethod method = new NovaMethod(this);
			for (int i = 0; i < keyValuePairs.length; i++) {
				method.deleteResource(service, resource + "/" + resourceId + "/metadata", keyValuePairs[i].getKey().toLowerCase(), null);
			}
		} catch( Exception e ) {
			logger.error("Error while removing tags from " + resource + " - " + resourceId + ".", e);
		}
    }
    
    public ProviderContext getContext() {
    	return context;
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
    public void extendSession() throws AdapterException {
    	// TODO Auto-generated method stub
    	
    }
    
    @Override
    public String getCloudVersion() throws AdapterException {
    	// TODO Auto-generated method stub
    	return null;
    }
    
    @Override
    public IOrganization getCurrentOrganization() throws AdapterException {
    	// TODO Auto-generated method stub
    	return null;
    }
    
    @Override
    public IOrganization getOrganizationByName(String name)
    		throws AdapterException {
    	// TODO Auto-generated method stub
    	return null;
    }
    
    @Override
    public IOrganization[] getOrganizations() throws AdapterException {
    	// TODO Auto-generated method stub
    	return null;
    }
    
    @Override
    public VmFlavor getVmFlavor(String vmFlavorId) throws AdapterException {
    	// TODO Auto-generated method stub
    	return null;
    }
    
    @Override
    public List<VmFlavor> getVmFlavors() throws AdapterException {
    	// TODO Auto-generated method stub
    	return null;
    }
    
    @Override
    public boolean isConnected() throws AdapterException {
    	// TODO Auto-generated method stub
    	return false;
    }
    
}
