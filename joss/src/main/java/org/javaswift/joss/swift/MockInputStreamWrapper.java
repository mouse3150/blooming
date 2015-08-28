package org.javaswift.joss.swift;

import org.javaswift.joss.command.impl.object.InputStreamWrapper;

import java.io.IOException;
import java.io.InputStream;

public class MockInputStreamWrapper extends InputStreamWrapper {

    public MockInputStreamWrapper(InputStream inputStream) {
        super(null, inputStream);
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
