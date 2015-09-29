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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.CloudErrorType;

public class NovaException extends AdapterException {
    private static final long serialVersionUID = -9188123825416917437L;
    private static Logger logger = (Logger) LoggerFactory.getLogger(NovaException.class);

    static public class ExceptionItems {
        public CloudErrorType type;
        public int code;
        public String message;
        public String details;
    }

    //{"badRequest": {"message": "AddressLimitExceeded: Address quota exceeded. You cannot allocate any more addresses", "code": 400}}
    static public ExceptionItems parseException(int code, String json) {
        ExceptionItems items = new ExceptionItems();
        
        items.code = code;
        items.type = CloudErrorType.GENERAL;
        items.message = "unknown";
        items.details = "The cloud returned an error code with explanation: ";
        if (items.code == HttpStatus.SC_UNAUTHORIZED) {
            items.type = CloudErrorType.AUTHENTICATION;
        }
        if( json != null ) {
            try {
                JSONObject ob = new JSONObject(json);

                if( code == 400 && ob.has("badRequest") ) {
                    ob = ob.getJSONObject("badRequest");
                }
                if( code == 413 && ob.has("overLimit") ) {
                    ob = ob.getJSONObject("overLimit");
                }
                if( ob.has("message") ) {
                    items.message = ob.getString("message");
                    if( items.message == null ) {
                        items.message = "unknown";
                    }
                    else {
                        items.message = items.message.trim();
                    }
                }
                if (items.message.equals("unknown")) {
                    String[] names = JSONObject.getNames(ob);
                    for (String key : names) {
                        try {
                            JSONObject msg = ob.getJSONObject(key);
                            if (msg.has("message") && !msg.isNull("message")) {
                                items.message = msg.getString("message");
                            }
                        }
                        catch (JSONException e) {
                            items.message = ob.getString(key);
                        }
                    }
                }
                if( ob.has("details") ) {
                    items.details = items.details + ob.getString("details");
                }
                else {
                    items.details = items.details + "[" + code + "] " + items.message;
                }
                String t = items.message.toLowerCase().trim();

                if( code == 413 ) {
                    items.type = CloudErrorType.THROTTLING;
                }
                else if( t.startsWith("addresslimitexceeded") || t.startsWith("ramlimitexceeded")) {
                    items.type = CloudErrorType.QUOTA;
                }
                else if( t.equals("unauthorized") ) {
                    items.type = CloudErrorType.AUTHENTICATION;
                }
                else if( t.equals("serviceunavailable") ) {
                    items.type = CloudErrorType.CAPACITY;
                }
                else if( t.equals("badrequest") || t.equals("badmediatype") || t.equals("badmethod") || t.equals("notimplemented") ) {
                    items.type = CloudErrorType.COMMUNICATION;
                }
                else if( t.equals("overlimit") ) {
                    items.type = CloudErrorType.QUOTA;
                }
                else if( t.equals("servercapacityunavailable") ) {
                    items.type = CloudErrorType.CAPACITY;
                }
                else if( t.equals("itemnotfound") ) {
                    return null;
                }
            }
            catch( JSONException e ) {
            	logger.error("parseException(): Invalid JSON in cloud response: " + json);
                items.details = items.details+" "+json;
            }
        }
        return items;
    }
    
    public NovaException(ExceptionItems items) {
        super(items.type, items.code, items.message, items.details);
    }
    
    public NovaException(CloudErrorType type, int code, String message, String details) {
        super(type, code, message, details);
    }
}
