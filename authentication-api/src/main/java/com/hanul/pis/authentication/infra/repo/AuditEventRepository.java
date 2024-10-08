package com.hanul.pis.authentication.infra.repo;

import com.hanul.pis.authentication.infra.entity.AuditEventEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditEventRepository extends CrudRepository<AuditEventEntity, Integer> {
    @Query("select count(*) from audit_event")
    public Integer countAll();
}
