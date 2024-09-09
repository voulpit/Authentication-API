package com.hanul.pis.authentication.model.dto;

import com.hanul.pis.authentication.utils.Constants;

public class OperationStatusDto {
    private String userId;
    private Constants.Operation operationName;
    private boolean successful;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Constants.Operation getOperationName() {
        return operationName;
    }

    public void setOperationName(Constants.Operation operationName) {
        this.operationName = operationName;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
