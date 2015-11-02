/*
 * Copyright (c) 2008-2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.09.29
 */
package cn.com.esrichina.adapter.openstack;


import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.CloudErrorType;
import cn.com.esrichina.adapter.utils.Cache;
import cn.com.esrichina.adapter.utils.CacheLevel;
import cn.com.esrichina.adapter.utils.time.Day;
import cn.com.esrichina.adapter.utils.time.TimePeriod;

public class NovaMethod extends AbstractMethod {
    public NovaMethod(NovaOpenStack openstack) { super(openstack); }
    
    public void deleteServers(final String resource, final String resourceId) throws AdapterException {
        AuthenticationContext context = openstack.getAuthenticationContext();
        String endpoint = context.getComputeUrl();
        
        if( endpoint == null ) {
            throw new AdapterException("No compute endpoint exists");
        }
        try {
            delete(context.getAuthToken(), endpoint, resource + "/" + resourceId);
        }
        catch (NovaException ex) {
            if (ex.getHttpCode() == HttpStatus.SC_UNAUTHORIZED) {
                Cache<AuthenticationContext> cache = Cache.getInstance(openstack, "authenticationContext", AuthenticationContext.class, CacheLevel.REGION_ACCOUNT, new TimePeriod<Day>(1, TimePeriod.DAY));
                cache.clear();
                deleteServers(resource, resourceId);
            }
            else {
                throw ex;
            }
        }
    }

    public void deleteNetworks(final String resource, final String resourceId) throws AdapterException {
        AuthenticationContext context = openstack.getAuthenticationContext();
        String endpoint = context.getNetworkUrl();

        if( endpoint == null ) {
            throw new AdapterException("No network endpoint exists");
        }
        if (resource != null && (!endpoint.endsWith("/") && !resource.startsWith("/"))) {
            endpoint = endpoint+"/";
        }
        try {
            delete(context.getAuthToken(), endpoint, resource + "/" + resourceId);
        }
        catch (NovaException ex) {
            if (ex.getHttpCode() == HttpStatus.SC_UNAUTHORIZED) {
                Cache<AuthenticationContext> cache = Cache.getInstance(openstack, "authenticationContext", AuthenticationContext.class, CacheLevel.REGION_ACCOUNT, new TimePeriod<Day>(1, TimePeriod.DAY));
                cache.clear();
                deleteNetworks(resource, resourceId);
            }
            else {
                throw ex;
            }
        }
    }

    public JSONObject getPorts(final String resource, final String resourceId) throws AdapterException {
        AuthenticationContext context = openstack.getAuthenticationContext();
        String endpoint = context.getComputeUrl();

        if( endpoint == null ) {
            throw new AdapterException("No compute URL has been established in " + context.getMyRegion());
        }
        String resourceUri = resource;
        if( resourceId != null ) {
            resourceUri += "/" + resourceId;
        }

        try {
            String response = getString(context.getAuthToken(), endpoint, resourceUri);

            if( response == null ) {
                return null;
            }
            try {
                return new JSONObject(response);
            }
            catch( JSONException e ) {
                throw new AdapterException(CloudErrorType.COMMUNICATION, 200, "invalidJson", response);
            }
        }
        catch (NovaException ex) {
            if (ex.getHttpCode() == HttpStatus.SC_UNAUTHORIZED) {
                Cache<AuthenticationContext> cache = Cache.getInstance(openstack, "authenticationContext", AuthenticationContext.class, CacheLevel.REGION_ACCOUNT, new TimePeriod<Day>(1, TimePeriod.DAY));
                cache.clear();
                return getPorts(resource, resourceId);
            }
            else {
                throw ex;
            }
        }
    }
    
    public JSONObject getServers(final String resource, final String resourceId, final boolean suffix) throws AdapterException {
        AuthenticationContext context = openstack.getAuthenticationContext();
        String endpoint = context.getComputeUrl();
        
        if( endpoint == null ) {
            throw new AdapterException("No compute URL has been established in " + context.getMyRegion());
        }
        String resourceUri = resource; // make a copy in case we need to retry with the original resource
        if( resourceId != null ) {
            resourceUri += "/" + resourceId;
        }
        else if( suffix ) {
            resourceUri += "/detail";
        }
        try {
            String response = getString(context.getAuthToken(), endpoint, resourceUri);

            if( response == null ) {
                return null;
            }
            try {
                return new JSONObject(response);
            }
            catch( JSONException e ) {
                throw new AdapterException(CloudErrorType.COMMUNICATION, 200, "invalidJson", response);
            }
        }
        catch (NovaException ex) {
            if (ex.getHttpCode() == HttpStatus.SC_UNAUTHORIZED) {
                Cache<AuthenticationContext> cache = Cache.getInstance(openstack, "authenticationContext", AuthenticationContext.class, CacheLevel.REGION_ACCOUNT, new TimePeriod<Day>(1, TimePeriod.DAY));
                cache.clear();
                return getServers(resource, resourceId, suffix);
            }
            else {
                throw ex;
            }
        }
    }

    public JSONObject getNetworks(final String resource, final String resourceId, final boolean suffix) throws AdapterException {
        return getNetworks(resource, resourceId, suffix, null);
    }

    public JSONObject getNetworks(final String resource, final String resourceId, final boolean suffix, final String query) throws AdapterException {
        AuthenticationContext context = openstack.getAuthenticationContext();
        String endpoint = context.getNetworkUrl();

        if( endpoint == null ) {
            throw new AdapterException("No network URL has been established in " + context.getMyRegion());
        }
        String resourceUri = resource; // make a copy in case we need to retry with the original resource
        if( resourceId != null ) {
            resourceUri += "/" + resourceId;
        }
        else if( suffix ) {
            resourceUri += "/detail";
        }
        if( query != null ) {
            resourceUri += query;
        }
        if (resourceUri != null && (!endpoint.endsWith("/") && !resourceUri.startsWith("/"))) {
            endpoint = endpoint+"/";
        }
        try {
            String response = getString(context.getAuthToken(), endpoint, resourceUri);

            if( response == null ) {
                return null;
            }
            try {
                return new JSONObject(response);
            }
            catch( JSONException e ) {
                throw new AdapterException(CloudErrorType.COMMUNICATION, 200, "invalidJson", response);
            }
        }
        catch (NovaException ex) {
            if (ex.getHttpCode() == HttpStatus.SC_UNAUTHORIZED) {
                Cache<AuthenticationContext> cache = Cache.getInstance(openstack, "authenticationContext", AuthenticationContext.class, CacheLevel.REGION_ACCOUNT, new TimePeriod<Day>(1, TimePeriod.DAY));
                cache.clear();
                return getNetworks(resource, resourceId, suffix, query);
            }
            else {
                throw ex;
            }
        }
    }

    public String postServersForString(final String resource, final String resourceId, final JSONObject body, final boolean suffix) throws AdapterException {
        AuthenticationContext context = openstack.getAuthenticationContext();

        String resourceUri = resource;
        if( resourceId != null ) {
            resourceUri += "/" + (suffix ? (resourceId + "/action") : resourceId);
        }
        String computeEndpoint = context.getComputeUrl();

        if( computeEndpoint == null ) {
            throw new AdapterException("No compute endpoint exists");
        }
        try {
            return postString(context.getAuthToken(), computeEndpoint, resourceUri, body.toString());
        }
        catch (NovaException ex) {
            if (ex.getHttpCode() == HttpStatus.SC_UNAUTHORIZED) {
                Cache<AuthenticationContext> cache = Cache.getInstance(openstack, "authenticationContext", AuthenticationContext.class, CacheLevel.REGION_ACCOUNT, new TimePeriod<Day>(1, TimePeriod.DAY));
                cache.clear();
                return postServersForString(resource, resourceId, body, suffix);
            }
            else {
                throw ex;
            }
        }
    }

    public JSONObject postServers(final String resource, final String resourceId, final JSONObject body, final boolean suffix) throws AdapterException {
        AuthenticationContext context = openstack.getAuthenticationContext();

        String resourceUri = resource;
        if( resourceId != null ) {
            resourceUri += "/" + (suffix ? (resourceId + "/action") : resourceId);
        }
        String computeEndpoint = context.getComputeUrl();

        if( computeEndpoint == null ) {
            throw new AdapterException("No compute endpoint exists");
        }
        try {
            String response = postString(context.getAuthToken(), computeEndpoint, resourceUri, body.toString());
        
            if( response == null ) {
                return null;
            }
            try {
                return new JSONObject(response);
            }
            catch( JSONException e ) {
                throw new AdapterException(CloudErrorType.COMMUNICATION, 200, "invalidJson", response);
            }
        }
        catch (NovaException ex) {
            if (ex.getHttpCode() == HttpStatus.SC_UNAUTHORIZED) {
                Cache<AuthenticationContext> cache = Cache.getInstance(openstack, "authenticationContext", AuthenticationContext.class, CacheLevel.REGION_ACCOUNT, new TimePeriod<Day>(1, TimePeriod.DAY));
                cache.clear();
                return postServers(resource, resourceId, body, suffix);
            }
            else {
                throw ex;
            }
        }
    }

    public JSONObject postNetworks(final String resource, final String resourceId, final JSONObject body, final String action) throws AdapterException {
        AuthenticationContext context = openstack.getAuthenticationContext();

        String resourceUri = resource;
        if( resourceId != null ) {
            resourceUri = resource + "/" + (action != null ? (resourceId + "/" + action) : resourceId);
        }
        String endpoint = context.getNetworkUrl();

        if( endpoint == null ) {
            throw new AdapterException("No network endpoint exists");
        }

        if (resourceUri != null && (!endpoint.endsWith("/") && !resourceUri.startsWith("/"))) {
            endpoint = endpoint+"/";
        }
        try {
            String response = postString(context.getAuthToken(), endpoint, resourceUri, body.toString());

            if( response == null ) {
                return null;
            }
            try {
                return new JSONObject(response);
            }
            catch( JSONException e ) {
                throw new AdapterException(CloudErrorType.COMMUNICATION, 200, "invalidJson", response);
            }
        }
        catch (NovaException ex) {
            if (ex.getHttpCode() == HttpStatus.SC_UNAUTHORIZED) {
                Cache<AuthenticationContext> cache = Cache.getInstance(openstack, "authenticationContext", AuthenticationContext.class, CacheLevel.REGION_ACCOUNT, new TimePeriod<Day>(1, TimePeriod.DAY));
                cache.clear();
                return postNetworks(resource, resourceId, body, action);
            }
            else {
                throw ex;
            }
        }
    }

    public JSONObject putNetworks(final String resource, final String resourceId, final JSONObject body, final String action) throws AdapterException {
        AuthenticationContext context = openstack.getAuthenticationContext();

        String resourceUri = resource;
        if( resourceId != null ) {
            resourceUri = resource + "/" + (action != null ? (resourceId + "/" + action) : resourceId);
        }
        String endpoint = context.getNetworkUrl();

        if( endpoint == null ) {
            throw new AdapterException("No network endpoint exists");
        }

        if (resourceUri != null && (!endpoint.endsWith("/") && !resourceUri.startsWith("/"))) {
            endpoint = endpoint+"/";
        }
        try {
            String response = putString(context.getAuthToken(), endpoint, resourceUri, body.toString());

            if( response == null ) {
                return null;
            }
            try {
                return new JSONObject(response);
            }
            catch( JSONException e ) {
                throw new AdapterException(CloudErrorType.COMMUNICATION, 200, "invalidJson", response);
            }
        }
        catch (NovaException ex) {
            if (ex.getHttpCode() == HttpStatus.SC_UNAUTHORIZED) {
                Cache<AuthenticationContext> cache = Cache.getInstance(openstack, "authenticationContext", AuthenticationContext.class, CacheLevel.REGION_ACCOUNT, new TimePeriod<Day>(1, TimePeriod.DAY));
                cache.clear();
                return putNetworks(resource, resourceId, body, action);
            }
            else {
                throw ex;
            }
        }
    }

    public JSONObject postNetworks(final String resource, final String resourceId, final JSONObject body, final boolean suffix) throws AdapterException {
        return postNetworks(resource, resourceId, body, suffix ? "action" : null);
    }
   
}

