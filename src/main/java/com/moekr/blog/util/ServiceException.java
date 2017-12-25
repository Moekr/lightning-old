package com.moekr.blog.util;

public class ServiceException extends RuntimeException {
    static final int BAD_REQUEST = 400;
    static final int NOT_FOUND = 404;

    private int error;

    ServiceException(int error, String message) {
        super(message);
        this.error = error;
    }

    public int getError() {
        return error;
    }
}
