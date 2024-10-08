package com.hanul.pis.authentication.service;

import com.hanul.pis.authentication.infra.entity.UserEntity;
import com.hanul.pis.authentication.model.dto.shared.AuditDto;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.utils.AuditEvent;

import java.util.List;

public interface AuditService {
    void insertAudit(UserEntity agentUser, UserEntity affectedUser, AuditEvent auditEvent, Boolean success, String comments);
    void insertAudit2(UserDto agentUser, UserDto affectedUser, AuditEvent auditEvent, Boolean success, String comments);
    List<AuditDto> getAuditForAffectedUser(String userId);
}
