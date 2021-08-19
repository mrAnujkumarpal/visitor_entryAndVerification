package com.vevs.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class VmsException extends RuntimeException {
    public VmsException(String message) {
        super(message);
    }

    public VmsException(String message, Throwable cause) {
        super(message, cause);
    }
}
