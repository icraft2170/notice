package com.rest.notice.exception;

public class FileUploadFailException extends RuntimeException {
    public FileUploadFailException() {
        super();
    }

    public FileUploadFailException(String message) {
        super(message);
    }

    public FileUploadFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileUploadFailException(Throwable cause) {
        super(cause);
    }

    protected FileUploadFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
