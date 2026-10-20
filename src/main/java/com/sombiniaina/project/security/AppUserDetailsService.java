package com.sombiniaina.project.security;

import com.sombiniaina.project.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
  private final AppUserRepository appUserRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var appUser =
        appUserRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Unknown user: " + username));
    return org.springframework.security.core.userdetails.User.builder()
        .username(appUser.getUsername())
        .password(appUser.getPasswordHash())
        .roles(appUser.getRole().name())
        .build();
  }
}
