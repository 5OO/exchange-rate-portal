package org.sberp.exchangerateportal.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
