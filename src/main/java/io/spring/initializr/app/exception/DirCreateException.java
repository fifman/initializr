package io.spring.initializr.app.exception;

public class DirCreateException extends RuntimeException {

    public DirCreateException(String path) {
        super("cannot create directory: " + path);
    }

}
