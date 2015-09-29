/*
 * Copyright (c) 2008-2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.09.29
 */
package cn.com.esrichina.adapter;

public class AdapterException extends Exception {
	
	private CloudErrorType errorType;
    private int            httpCode;
    private String         providerCode;

	private static final long serialVersionUID = -6371427172486913862L;

	public AdapterException() {
	}

	public AdapterException(String message) {
		super(message);
	}

	public AdapterException(Throwable cause) {
		super(cause);
	}

	public AdapterException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
	/**
     * Constructs a cloud adapter exception with cloud provider data added in
     * @param type cloud adapter error type
     * @param httpCode the HTTP error code
     * @param providerCode the provider-specific error code
     * @param msg the error message
     */
    public AdapterException(CloudErrorType type, int httpCode, String providerCode, String msg) {
        super(msg);
        this.errorType = type;
        this.httpCode = httpCode;
        this.providerCode = providerCode;
    }

    /**
     * Constructs a cloud adapter exception with cloud provider data added in
     * @param type cloud adapter error type
     * @param httpCode the HTTP error code
     * @param providerCode the provider-specific error code
     * @param msg the error message
     * @param cause the error that caused this exception to be thrown
     */
    public AdapterException(CloudErrorType type, int httpCode, String providerCode, String msg, Throwable cause) {
        super(msg, cause);
        this.errorType = type;
        this.httpCode = httpCode;
        this.providerCode = providerCode;
    }
    
    public int getHttpCode() {
        return httpCode;
    }
    
    public CloudErrorType getErrorType() {
        return (errorType == null ? CloudErrorType.GENERAL : errorType);
    }
    
    public String getProviderCode() {
        return (providerCode == null ? "" : providerCode);
    }

}
