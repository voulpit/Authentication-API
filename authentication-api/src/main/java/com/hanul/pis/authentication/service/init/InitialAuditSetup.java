package com.hanul.pis.authentication.service.init;

import com.hanul.pis.authentication.infra.entity.AuditEventEntity;
import com.hanul.pis.authentication.infra.repo.AuditEventRepository;
import com.hanul.pis.authentication.utils.AuditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitialAuditSetup {
    @Autowired
    private AuditEventRepository auditEventRepository;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        int dbCount = auditEventRepository.countAll();
        if (dbCount != AuditEvent.class.getEnumConstants().length) {
            auditEventRepository.deleteAll();
            AuditEvent[] auditEvents = AuditEvent.class.getEnumConstants();
            for (int i = 0; i < auditEvents.length; i++) {
                AuditEventEntity auditEventEntity = new AuditEventEntity();
                auditEventEntity.setId(i + 1);
                auditEventEntity.setName(auditEvents[i].getName());
                auditEventRepository.save(auditEventEntity);
            }
        }
    }
}
