package com.hanul.pis.authentication.model.dto.mapper;

import com.hanul.pis.authentication.infra.entity.AuditEntity;
import com.hanul.pis.authentication.model.dto.shared.AuditDto;
import com.hanul.pis.authentication.utils.AuditEvent;

public class AuditMapper {

    public static AuditDto mapAuditEntityToAuditDto(AuditEntity auditEntity) {
        AuditDto auditDto = new AuditDto();
        auditDto.setAffectedUserId(auditEntity.getAffectedUser().getUserId());
        auditDto.setEvent(AuditEvent.getName(auditEntity.getEvent().getId()));
        auditDto.setComments(auditEntity.getComments());
        auditDto.setSuccess(auditEntity.getSuccess());
        auditDto.setTimestamp(auditEntity.getTimestamp());
        auditDto.setAgentUserId(auditEntity.getAgentUser().getUserId());
        return auditDto;
    }
}
