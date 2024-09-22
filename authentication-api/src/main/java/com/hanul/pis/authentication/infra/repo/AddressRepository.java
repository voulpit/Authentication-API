package com.hanul.pis.authentication.infra.repo;

import com.hanul.pis.authentication.infra.entity.AddressEntity;
import com.hanul.pis.authentication.infra.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByUserEntity(UserEntity userEntity);
    AddressEntity findByPublicId(String addressId);
}
