package com.hanul.pis.authentication.infra.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id; // real userId

    @Column(nullable = false)
    private String userId; // public user id (alias pt id real)

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 120, unique = true)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    private Boolean emailVerificationStatus = false;

    private String emailVerificationToken;

    private String passwordResetToken;

    @Column(nullable = false)
    private Boolean activeInd = false;

    @Column(nullable = false)
    private Boolean deletedInd = false;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL) // propagate all operations to them too
    private List<AddressEntity> addresses;

    @ManyToMany(fetch = FetchType.EAGER)  // roles immediately available at authentication
    @JoinTable(name="users_roles",
               joinColumns = @JoinColumn(name="users_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name="roles_id", referencedColumnName = "id"))
    private List<RoleEntity> roles;

    @OneToMany(mappedBy = "affectedUser")
    private List<AuditEntity> auditEntities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public Boolean getEmailVerificationStatus() {
        return emailVerificationStatus;
    }

    public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
        this.emailVerificationStatus = emailVerificationStatus;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public Boolean getActiveInd() {
        return activeInd;
    }

    public void setActiveInd(Boolean activeInd) {
        this.activeInd = activeInd;
    }

    public Boolean getDeletedInd() {
        return deletedInd;
    }

    public void setDeletedInd(Boolean deletedInd) {
        this.deletedInd = deletedInd;
    }

    public List<AddressEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressEntity> addresses) {
        this.addresses = addresses;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }

    public List<AuditEntity> getAuditEntities() {
        return auditEntities;
    }

    public void setAuditEntities(List<AuditEntity> auditEntities) {
        this.auditEntities = auditEntities;
    }
}
