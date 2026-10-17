package com.sombiniaina.project.service;

import com.sombiniaina.project.mapper.AppUserMapper;
import com.sombiniaina.project.model.AppUser;
import com.sombiniaina.project.model.Role;
import com.sombiniaina.project.repository.AppUserRepository;
import com.sombiniaina.project.repository.model.JAppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppUserMapper appUserMapper;

    @Transactional
    public AppUser createUser(
            String username,
            String email,
            String rawPassword
    ){
        JAppUser jAppUser =
                JAppUser.builder()
                        .id(UUID.randomUUID())
                        .username(username)
                        .email(email)
                        .passwordHash(passwordEncoder.encode(rawPassword))
                        .role(Role.USER)
                        .build();
        return appUserMapper.toModel(appUserRepository.save(jAppUser));
    }

    @Transactional
    public AppUser createAdmin(
            String username,
            String email,
            String rawPassword
    ){
        JAppUser jAppUser =
                JAppUser.builder()
                        .id(UUID.randomUUID())
                        .username(username)
                        .email(email)
                        .passwordHash(passwordEncoder.encode(rawPassword))
                        .role(Role.ADMIN)
                        .build();
        return appUserMapper.toModel(appUserRepository.save(jAppUser));
    }
}
