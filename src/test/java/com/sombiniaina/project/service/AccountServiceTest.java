package com.sombiniaina.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sombiniaina.project.exception.AccountAlreadyExistsException;
import com.sombiniaina.project.mapper.AppUserMapper;
import com.sombiniaina.project.model.AppUser;
import com.sombiniaina.project.model.Role;
import com.sombiniaina.project.repository.AppUserRepository;
import com.sombiniaina.project.repository.model.JAppUser;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock private AppUserRepository appUserRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private AppUserMapper appUserMapper;
  @InjectMocks private AccountService accountService;

  @Test
  void addAccount_throwsWhenUsernameAlreadyTaken() {
    when(appUserRepository.existsByUsername("alice")).thenReturn(true);

    assertThatThrownBy(
            () -> accountService.addAccount("alice", "alice@example.com", "secret", Role.USER))
        .isInstanceOf(AccountAlreadyExistsException.class)
        .hasMessageContaining("username");

    verify(appUserRepository, never()).save(any());
  }

  @Test
  void addAccount_throwsWhenEmailAlreadyTaken() {
    when(appUserRepository.existsByEmail("alice@example.com")).thenReturn(true);

    assertThatThrownBy(
            () -> accountService.addAccount("alice", "alice@example.com", "secret", Role.USER))
        .isInstanceOf(AccountAlreadyExistsException.class)
        .hasMessageContaining("email");

    verify(appUserRepository, never()).save(any());
  }

  @Test
  void addAccount_hashesThePassword() {
    when(passwordEncoder.encode("secret")).thenReturn("hashed-secret");
    when(appUserRepository.save(any(JAppUser.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    accountService.addAccount("alice", "alice@example.com", "secret", Role.USER);

    ArgumentCaptor<JAppUser> captor = ArgumentCaptor.forClass(JAppUser.class);
    verify(appUserRepository).save(captor.capture());
    assertThat(captor.getValue().getPasswordHash()).isEqualTo("hashed-secret");
    assertThat(captor.getValue().getPasswordHash()).isNotEqualTo("secret");
  }

  @Test
  void addAccount_usesTheRequestedRole() {
    when(appUserRepository.save(any(JAppUser.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    accountService.addAccount("alice", "alice@example.com", "secret", Role.ADMIN);

    ArgumentCaptor<JAppUser> captor = ArgumentCaptor.forClass(JAppUser.class);
    verify(appUserRepository).save(captor.capture());
    assertThat(captor.getValue().getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  void addAccount_savesAndReturnsMappedAccount() {
    when(appUserRepository.save(any(JAppUser.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    AppUser expected =
        AppUser.builder()
            .id(UUID.randomUUID())
            .username("alice")
            .email("alice@example.com")
            .passwordHash("hashed-secret")
            .role(Role.USER)
            .build();
    when(appUserMapper.toModel(any(JAppUser.class))).thenReturn(expected);

    AppUser result = accountService.addAccount("alice", "alice@example.com", "secret", Role.USER);

    assertThat(result).isEqualTo(expected);
    verify(appUserRepository).save(any(JAppUser.class));
  }
}
