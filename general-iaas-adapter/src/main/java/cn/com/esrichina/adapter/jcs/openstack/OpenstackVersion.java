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
        	throw new JcsOpenstackException("dont support openstack version.");
        }
	}
}
