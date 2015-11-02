/*
 * Copyright (c) 2008-2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.10.25
 */
package cn.com.esrichina.adapter.jcs.openstack;

public enum OpenstackVersion {
	JUNO, KILO, LIBERTY;
	
	public OpenstackVersion getOpenstackVersion(String version) throws JcsOpenstackException {
		if( version.equalsIgnoreCase("juno") ) {
            return JUNO;
        }
        else if( version.equalsIgnoreCase("kilo") ) {
            return KILO;
        }
        else if( version.equalsIgnoreCase("liberty") ) {
            return LIBERTY;
        } else {
        	throw new JcsOpenstackException("don't support openstack version.");
        }
	}
}
