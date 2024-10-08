package com.hanul.pis.authentication.infra.entity;

import com.hanul.pis.authentication.utils.Constants;
import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "audit")
public class AuditEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agent_user")
    private UserEntity agentUser; // doer

    @ManyToOne
    @JoinColumn(name = "affected_user")
    private UserEntity affectedUser; // user done upon

    @ManyToOne
    @JoinColumn(name = "event_id")
    private AuditEventEntity event;

    private Boolean success;

    private Date timestamp;

    @Column(length = Constants.AUDIT_COMMENTS_LENGTH)
    private String comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getAgentUser() {
        return agentUser;
    }

    public void setAgentUser(UserEntity activeUser) {
        this.agentUser = activeUser;
    }

    public UserEntity getAffectedUser() {
        return affectedUser;
    }

    public void setAffectedUser(UserEntity passiveUser) {
        this.affectedUser = passiveUser;
    }

    public AuditEventEntity getEvent() {
        return event;
    }

    public void setEvent(AuditEventEntity event) {
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
