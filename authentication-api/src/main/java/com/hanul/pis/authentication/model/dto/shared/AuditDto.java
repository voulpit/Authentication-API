package com.hanul.pis.authentication.model.dto.shared;

import java.util.Date;

public class AuditDto {
    private String agentUserId;
    private String affectedUserId;
    private String event;
    private Boolean success;
    private Date timestamp;
    private String comments;

    public String getAgentUserId() {
        return agentUserId;
    }

    public void setAgentUserId(String agentUserId) {
        this.agentUserId = agentUserId;
    }

    public String getAffectedUserId() {
        return affectedUserId;
    }

    public void setAffectedUserId(String affectedUserId) {
        this.affectedUserId = affectedUserId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
