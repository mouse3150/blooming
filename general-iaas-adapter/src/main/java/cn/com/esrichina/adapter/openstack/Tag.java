package cn.com.esrichina.adapter.openstack;


import java.io.Serializable;

/**
 * A key/value pair for use in cloud resource meta-data.
 * @author George Reese
 * @version 2013.04 added javadoc
 * @since unknown
 */
public class Tag implements Comparable<Tag> {
    private String key;
    private String value;

    /**
     * Constructs an empty tag with no key or value.
     */
    public Tag() { }

    /**
     * Constructs a tag with the specified key/value pair.
     * @param key the key for this pair
     * @param value the value to be associated with the key
     */
    public Tag(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    @Override
    public int compareTo(Tag tag) {
        int x = key.compareTo(tag.key);
        
        if( x != 0 ) {
            return x;
        }
        if( value == null ) {
            return "".compareTo(tag.value == null ? "" : tag.value);
        }
        else {
            return value.compareTo(tag.value == null ? "" : tag.value);
        }
    }

    @Override
    public boolean equals(Object ob) {
        if( ob == null ) {
            return false;
        }
        if( ob == this ) {
            return true;
        }
        if( !ob.getClass().getName().equals(getClass().getName()) ) {
            return false;
        }
        Tag tag = (Tag)ob;
        if( !(key == null ? "" : key).equals(tag.key == null ? "" : tag.key) ) {
            return false;
        }
        return (value == null ? "" : value).equals(tag.value == null ? "" : tag.value);
    }

    /**
     * Sets the key associated with this tag.
     * @param key the key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the key for this key/value pair
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value associated with this key/value pair.
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the value associated with this key/value pair
     */
    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return (key + "=" + value);
    }
}
