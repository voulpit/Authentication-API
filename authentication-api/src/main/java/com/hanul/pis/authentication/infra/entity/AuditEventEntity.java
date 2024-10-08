package com.hanul.pis.authentication.infra.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity(name = "audit_event")
public class AuditEventEntity {
    @Id
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<AuditEntity> auditEntities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AuditEntity> getAuditEntities() {
        return auditEntities;
    }

    public void setAuditEntities(List<AuditEntity> auditEntities) {
        this.auditEntities = auditEntities;
    }
}
