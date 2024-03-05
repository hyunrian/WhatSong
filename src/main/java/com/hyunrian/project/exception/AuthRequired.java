package com.hyunrian.project.exception;

public class AuthRequired extends RuntimeException{

    int code;
    public AuthRequired() {
        super();
    }

    public AuthRequired(String message) {
        super(message);
    }

    public AuthRequired(String message, int code) {
        super(message);
        this.code = code;
    }

    public AuthRequired(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthRequired(Throwable cause) {
        super(cause);
    }
}
