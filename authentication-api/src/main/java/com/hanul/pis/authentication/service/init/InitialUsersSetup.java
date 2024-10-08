package com.hanul.pis.authentication.service.init;

import com.hanul.pis.authentication.infra.entity.AuthorityEntity;
import com.hanul.pis.authentication.infra.entity.RoleEntity;
import com.hanul.pis.authentication.infra.entity.UserEntity;
import com.hanul.pis.authentication.infra.repo.AuthorityRepository;
import com.hanul.pis.authentication.infra.repo.RoleRepository;
import com.hanul.pis.authentication.infra.repo.UserRepository;
import com.hanul.pis.authentication.utils.RegistrationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.hanul.pis.authentication.security.SecurityConstants.*;

@Component
public class InitialUsersSetup {
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RegistrationUtils registrationUtils;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        AuthorityEntity readAuth = createAuthority(READ_AUTHORITY);
        AuthorityEntity writeAuth = createAuthority(WRITE_AUTHORITY);
        AuthorityEntity deleteAuth = createAuthority(DELETE_AUTHORITY);
        AuthorityEntity auditAuth = createAuthority(AUDIT_AUTHORITY);

        RoleEntity userRole = createRole(USER_ROLE, Arrays.asList(readAuth, writeAuth));
        RoleEntity adminRole = createRole(ADMIN_ROLE, Arrays.asList(readAuth, writeAuth, deleteAuth, auditAuth));
        RoleEntity inspectorRole = createRole(INSPECTOR_ROLE, Arrays.asList(readAuth, auditAuth));

        createAdminUser(adminRole);
        createInspector(inspectorRole);
    }

    private void createInspector(RoleEntity inspectorRole) {
        UserEntity adminUser = userRepository.findByEmail("chitibus007@yahoo.com");
        if (adminUser == null) {
            adminUser = new UserEntity();
            adminUser.setFirstName("Mia");
            adminUser.setLastName("Kitsibush");
            adminUser.setEmail("chitibus007@yahoo.com");
            adminUser.setEmailVerificationStatus(true);
            adminUser.setActiveInd(true);
            adminUser.setUserId(registrationUtils.generateUserId(30));
            adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("clopotel"));
            adminUser.setRoles(List.of(inspectorRole));
            userRepository.save(adminUser);
        }
    }

    private void createAdminUser(RoleEntity adminRole) {
        UserEntity adminUser = userRepository.findByEmail("ana@yahoo.com");
        if (adminUser == null) {
            adminUser = new UserEntity();
            adminUser.setFirstName("Ana");
            adminUser.setLastName("T.");
            adminUser.setEmail("ana@yahoo.com");
            adminUser.setEmailVerificationStatus(true);
            adminUser.setActiveInd(true);
            adminUser.setUserId(registrationUtils.generateUserId(30));
            adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("clopotel"));
            adminUser.setRoles(List.of(adminRole));
            userRepository.save(adminUser);
        }
    }

    private AuthorityEntity createAuthority(String name) {
        AuthorityEntity authorityEntity = authorityRepository.findByName(name);
        if (authorityEntity == null) {
            authorityEntity = new AuthorityEntity();
            authorityEntity.setName(name);
            authorityRepository.save(authorityEntity);
        }
        return authorityEntity;
    }

    private RoleEntity createRole(String name, List<AuthorityEntity> authorities) {
        RoleEntity role = roleRepository.findByName(name);
        if (role == null) {
            role = new RoleEntity();
            role.setName(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
