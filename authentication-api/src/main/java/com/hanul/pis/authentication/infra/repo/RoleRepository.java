package com.hanul.pis.authentication.infra.repo;

import com.hanul.pis.authentication.infra.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    RoleEntity findByName(String role);
}
