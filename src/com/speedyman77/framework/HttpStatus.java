package com.speedyman77.framework;

public enum HttpStatus {
    Ok(200),
    NotFound(404),
    InternalServerError(500);

    HttpStatus(int i) {
    }
}
