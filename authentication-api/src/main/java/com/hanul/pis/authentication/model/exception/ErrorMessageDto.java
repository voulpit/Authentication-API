package com.hanul.pis.authentication.model.exception;

import java.util.Date;

public class ErrorMessageDto {
    private String message;
    private Date timestamp;
    private int statusCode;

    public ErrorMessageDto() {
    }

    public ErrorMessageDto(String message, Date timestamp, int statusCode) {
        this.message = message;
        this.timestamp = timestamp;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
