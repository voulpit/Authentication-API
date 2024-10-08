package com.hanul.pis.authentication.security;

import com.hanul.pis.authentication.infra.entity.AuthorityEntity;
import com.hanul.pis.authentication.infra.entity.RoleEntity;
import com.hanul.pis.authentication.infra.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private final UserEntity userEntity;

    public UserPrincipal(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (userEntity.getRoles() == null) {
            return List.of();
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        userEntity.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorityEntities.addAll(role.getAuthorities());
        });
        authorityEntities.forEach(authorityEntity -> {
            authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
        });
        return authorities; // contains roles and associated authorities
    }

    @Override
    public String getPassword() {
        return userEntity.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return userEntity.getActiveInd();
    }

    public String getUserId() {
        return userEntity.getUserId();
    }
}
