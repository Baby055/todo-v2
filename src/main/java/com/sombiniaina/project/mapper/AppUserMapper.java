package com.sombiniaina.project.mapper;

import com.sombiniaina.project.model.AppUser;
import com.sombiniaina.project.repository.model.JAppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AppUserMapper {
    public List<AppUser> toModel(List<JAppUser> jAppUsers) {
        return jAppUsers.stream().map(this::toModel).toList();
    }

    public AppUser toModel(JAppUser jAppUser) {
        return AppUser.builder()
                .id(jAppUser.getId())
                .username(jAppUser.getUsername())
                .email(jAppUser.getEmail())
                .passwordHash(jAppUser.getPasswordHash())
                .role(jAppUser.getRole())
                .build();
    }

    public List<JAppUser> toEntity(List<AppUser> appUsers) {
        return appUsers.stream().map(this::toEntity).toList();
    }

    public JAppUser toEntity(AppUser appUser) {
        return JAppUser.builder()
                .id(appUser.id())
                .username(appUser.username())
                .email(appUser.email())
                .passwordHash(appUser.passwordHash())
                .role(appUser.role())
                .build();
    }
}
