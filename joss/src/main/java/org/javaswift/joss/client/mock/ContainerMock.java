package org.javaswift.joss.client.mock;

import org.javaswift.joss.client.core.AbstractContainer;
import org.javaswift.joss.model.StoredObject;

public class ContainerMock extends AbstractContainer {

    public ContainerMock(AccountMock account, String name) {
        super(account, name, ALLOW_CACHING);
    }

    @Override
    public StoredObject getObject(String objectName) {
        return new StoredObjectMock(this, objectName);
    }

}

