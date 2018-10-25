package com.crypto.cryptotrading.Exception;


//This needs to be pulled from common repo
public class ExceptionResponse {
    private String type;
    private String error;
    private String path;
    private Long timestamp;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String type, String error, String traceId, String path, Long timestamp) {
       this.type = type;
       this.error = error;
       this.path = path;
       this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
