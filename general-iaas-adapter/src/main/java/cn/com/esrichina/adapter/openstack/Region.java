package cn.com.esrichina.adapter.openstack;

import java.io.Serializable;

public class Region implements Serializable {
    private static final long serialVersionUID = 5759708802908910045L;
    
    private boolean       active;          
    private boolean       available; 
    private String        jurisdiction;
    private String        name;       
    private String        providerRegionId;

    public Region() { }

    public Region(String regionId, String name, boolean active, boolean available) {
        providerRegionId = regionId;
        this.name = name;
        this.active = active;
        this.available = available;
    }
    
    public boolean equals(Object ob) {
        if( ob == null ) {
            return false;
        }
        if( ob == this ) {
            return true;
        }
        if( !getClass().getName().equals(ob.getClass().getName()) ) {
            return false;
        }
        return getProviderRegionId().equals(((Region)ob).getProviderRegionId());
    }
    
    public String getName() {
        return name;
    }

    public String getProviderRegionId() {
        return providerRegionId;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setActive(boolean active) { 
        this.active = active;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProviderRegionId(String id) {
        providerRegionId = id;
    }
    
    public String toString() {
        return (name + " [#" + providerRegionId + "]");
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }
}
