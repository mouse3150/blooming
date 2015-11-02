/*
 * Copyright (c) 2008-2015, Esri(China), Inc. All Rights Reserved.
 * @author chenhao
 * @email chenh@esrichina.com.cn
 * @since 2015.09.29
 */
package cn.com.esrichina.adapter.openstack;

/**
 * 用于兼容不同厂商基于OpenStack发行版
 * 如：DELL, HP等；
 * 
 */
public enum OpenStackProvider {
    DELL, DREAMHOST, HP, IBM, METACLOUD, RACKSPACE, OPENSTACK, OTHER;
    static public OpenStackProvider getProvider(String name) {
        if( name.equalsIgnoreCase("dell") ) {
            return DELL;
        }
        else if( name.equalsIgnoreCase("dreamhost") ) {
            return DREAMHOST;
        }
        else if( name.equalsIgnoreCase("hp") ) {
            return HP;
        }
        else if( name.equalsIgnoreCase("ibm") ) {
            return IBM;
        }
        else if( name.equalsIgnoreCase("metacloud") ) {
            return METACLOUD;
        }
        else if( name.equalsIgnoreCase("rackspace") ) {
            return RACKSPACE;
        }
        else if( name.equalsIgnoreCase("openstack") ) {
            return OPENSTACK;
        }
        return OTHER;
    }

}
