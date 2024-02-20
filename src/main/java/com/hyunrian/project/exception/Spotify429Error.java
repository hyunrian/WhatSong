package com.hyunrian.project.exception;

public class Spotify429Error extends RuntimeException {

    int errorCode;

    public Spotify429Error() {
        super();
    }

    public Spotify429Error(String message) {
        super(message);
    }

    public Spotify429Error(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
