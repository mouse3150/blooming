package org.javaswift.joss.command.mock.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.command.mock.identity.AuthenticationCommandMock;
import org.javaswift.joss.command.shared.factory.AuthenticationCommandFactory;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.swift.Swift;

public class AuthenticationCommandFactoryMock implements AuthenticationCommandFactory {

    private Swift swift;

    public AuthenticationCommandFactoryMock(Swift swift) {
        this.swift = swift;
    }

    @Override
    public AuthenticationCommand createAuthenticationCommand(HttpClient httpClient, AuthenticationMethod authenticationMethod,
                                                             String url, String tenantName, String tenantId,
                                                             String username, String password, AuthenticationMethod.AccessProvider accessProvier) {
        return new AuthenticationCommandMock(swift, url, tenantName, tenantId, username, password);
    }

}
