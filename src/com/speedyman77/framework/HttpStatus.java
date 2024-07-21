package com.speedyman77.framework;

public enum HttpStatus {
    OK(200),
    NotFound(404),
    InternalServerError(500);

    private final int code;

    HttpStatus(int statusCode) {
        this.code = statusCode;
    }

    public int getStatusCode() {
        return this.code;
    }
}
