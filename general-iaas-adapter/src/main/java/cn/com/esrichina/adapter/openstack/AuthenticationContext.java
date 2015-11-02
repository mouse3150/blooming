/*
 * Copyright (c) 2008-2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.09.29
 */
package cn.com.esrichina.adapter.openstack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class AuthenticationContext { 
    private String                         authToken;
    private Map<String,Map<String,String>> endpoints;
    private String                         myRegion;
    private String                         storageToken;
    private String                         tenantId;

    public AuthenticationContext( String regionId,  String token,  String tenantId,  Map<String,Map<String,String>> services, String storageToken) {
        myRegion = regionId;
        authToken = token;
        endpoints = services;
        this.tenantId = tenantId;
        this.storageToken = storageToken;
    }

    public  String getAuthToken() {
        return authToken;
    }
    
    public String getComputeUrl() {
        Map<String,String> map = endpoints.get("compute");
        
        if( map == null ) {
            return null;
        }
        return map.get(myRegion);
    }

    public String getNetworkUrl() {
        return getServiceUrl("network");
    }

    public String getStorageToken() {
        if( storageToken == null ) {
            return getAuthToken();
        }
        return storageToken;
    }
        
    public String getStorageUrl() {
        return getServiceUrl("object-store");
    }

    public  String getTenantId() {
        return tenantId;
    }

    public  String getMyRegion() {
        return myRegion;
    }

    public String getServiceUrl(String service) {
        Map<String,String> map = endpoints.get(service);

        if( map == null ) {
            return null;
        }
        String endpoint = null;

        for( String key : map.keySet() ) {
            if( myRegion == null ) {
                myRegion = key;
            }
            if( key == null ) {
                endpoint = map.get(null);
            }
            else if( key.equals(myRegion) ) {
                return map.get(myRegion);
            }
            else if( myRegion.endsWith(key) ) {
                endpoint = map.get(key);
            }
        }
        return endpoint;
    }
    
    public  Collection<Region> listRegions() {
        Map<String,String> map = endpoints.get("compute");
        
        if( map == null ) {
            map = endpoints.get("object-store");
        }
        if( map == null ) {
            return Collections.emptyList();
        }
        ArrayList<Region> regions = new ArrayList<Region>();
        
        for( String regionId : map.keySet() ) {
            Region region = new Region();
        
            region.setActive(true);
            region.setAvailable(true);
            region.setJurisdiction(Jurisdiction.US.name());
            region.setName(regionId);
            region.setProviderRegionId(regionId);
            regions.add(region);
        }
        return regions;
    }
}
