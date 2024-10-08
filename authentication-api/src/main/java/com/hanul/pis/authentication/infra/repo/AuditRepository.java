package com.hanul.pis.authentication.infra.repo;

import com.hanul.pis.authentication.infra.entity.AuditEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends CrudRepository<AuditEntity, Long> {
}
