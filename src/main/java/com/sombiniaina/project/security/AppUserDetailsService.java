package com.sombiniaina.project.security;

import com.sombiniaina.project.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        var appUser =
                appUserRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Unknown user: " + username));
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(passwordEncoder.encode(appUser.getPasswordHash()))
                .roles("ROLE_" +  appUser.getRole())
                .build();
    }
}
