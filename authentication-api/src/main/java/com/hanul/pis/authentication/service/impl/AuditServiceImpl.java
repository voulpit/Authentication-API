package com.hanul.pis.authentication.service.impl;

import com.hanul.pis.authentication.infra.entity.AuditEntity;
import com.hanul.pis.authentication.infra.entity.AuditEventEntity;
import com.hanul.pis.authentication.infra.entity.UserEntity;
import com.hanul.pis.authentication.infra.repo.AuditEventRepository;
import com.hanul.pis.authentication.infra.repo.AuditRepository;
import com.hanul.pis.authentication.infra.repo.UserRepository;
import com.hanul.pis.authentication.model.dto.mapper.AuditMapper;
import com.hanul.pis.authentication.model.dto.shared.AuditDto;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.service.AuditService;
import com.hanul.pis.authentication.utils.AuditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.hanul.pis.authentication.utils.Constants.AUDIT_COMMENTS_LENGTH;

@Service
public class AuditServiceImpl implements AuditService {
    @Autowired
    private AuditRepository auditRepository;
    @Autowired
    private AuditEventRepository auditEventRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void insertAudit(UserEntity agentUser, UserEntity affectedUser, AuditEvent auditEvent, Boolean success, String comments) {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setAgentUser(agentUser);
        auditEntity.setAffectedUser(affectedUser);
        auditEntity.setSuccess(success);
        auditEntity.setComments(comments.substring(0, Math.min(AUDIT_COMMENTS_LENGTH, comments.length())));
        auditEntity.setTimestamp(new Date());

        Optional<AuditEventEntity> auditEventEntity = auditEventRepository.findById(auditEvent.getId());
        auditEventEntity.ifPresent(auditEntity::setEvent);

        auditRepository.save(auditEntity);
    }

    @Override
    public void insertAudit2(UserDto agentUser, UserDto affectedUser, AuditEvent auditEvent, Boolean success, String comments) {
        UserEntity agentUserEntity = userRepository.findByEmail(agentUser.getEmail());
        UserEntity affectedUserEntity = agentUser == affectedUser ? agentUserEntity :
                userRepository.findByEmail(affectedUser.getEmail());
        insertAudit(agentUserEntity, affectedUserEntity, auditEvent, success, comments);
    }

    @Override
    public List<AuditDto> getAuditForAffectedUser(String userId) {
        List<AuditDto> result = new ArrayList<>();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity != null) {
            List<AuditEntity> auditEntities = userEntity.getAuditEntities();
            auditEntities.forEach(auditEntity -> result.add(AuditMapper.mapAuditEntityToAuditDto(auditEntity)));
            return result;
        }
        return result;
    }
}
