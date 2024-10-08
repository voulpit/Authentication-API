package com.hanul.pis.authentication.infra.entity;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

@Entity(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="roles_authorities",
            joinColumns = @JoinColumn(name="roles_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="authority_id", referencedColumnName = "id"))
    private List<AuthorityEntity> authorities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public List<AuthorityEntity> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthorityEntity> authorities) {
        this.authorities = authorities;
    }
}
