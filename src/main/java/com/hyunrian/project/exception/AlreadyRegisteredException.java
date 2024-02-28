package com.hyunrian.project.exception;

import lombok.Getter;

@Getter
public class AlreadyRegisteredException extends RuntimeException {

    String type;
    public AlreadyRegisteredException() {
        super();
    }

    public AlreadyRegisteredException(String message) {
        super(message);
    }

    public AlreadyRegisteredException(String message, String type) {
        super(message);
        this.type = type;
    }


}
