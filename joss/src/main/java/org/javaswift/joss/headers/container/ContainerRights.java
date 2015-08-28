package org.javaswift.joss.headers.container;

import org.apache.http.HttpResponse;
import org.javaswift.joss.headers.Header;

/**
 * Allows the rights change of a container to and from public/private
 */
public class ContainerRights extends Header {

    public static final String X_CONTAINER_READ = "X-Container-Read";

    public static final String PUBLIC_CONTAINER = ".r:*";

    private boolean publicContainer;

    public ContainerRights(boolean publicContainer) {
        this.publicContainer = publicContainer;
    }

    public boolean isPublic() {
        return this.publicContainer;
    }

    @Override
    public String getHeaderValue() {
        return publicContainer ? PUBLIC_CONTAINER : "";
    }

    @Override
    public String getHeaderName() {
        return X_CONTAINER_READ;
    }

    public static ContainerRights fromResponse(HttpResponse response) {
        return new ContainerRights(headerNotEmpty(response, X_CONTAINER_READ));
    }

}
