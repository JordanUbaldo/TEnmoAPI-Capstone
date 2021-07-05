package com.techelevator.tenmo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Insufficient Funds!")
public class NsfException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NsfException() {
        super("Insufficient Funds!");
    }
}
