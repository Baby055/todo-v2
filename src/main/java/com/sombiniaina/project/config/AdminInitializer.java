package com.sombiniaina.project.config;

import com.sombiniaina.project.model.Role;
import com.sombiniaina.project.repository.AppUserRepository;
import com.sombiniaina.project.service.AccountService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminInitializer implements CommandLineRunner {
  private final AccountService accountService;
  private final AppUserRepository appUserRepository;
  private final String adminUsername;
  private final String adminEmail;
  private final String adminPassword;

  public AdminInitializer(
      AccountService accountService,
      AppUserRepository appUserRepository,
      @Value("${bootstrap.admin.username:admin}") String adminUsername,
      @Value("${bootstrap.admin.email:}") String adminEmail,
      @Value("${bootstrap.admin.password:}") String adminPassword) {
    this.accountService = accountService;
    this.appUserRepository = appUserRepository;
    this.adminUsername = adminUsername;
    this.adminEmail = adminEmail;
    this.adminPassword = adminPassword;
  }

  @Override
  public void run(String... args) {
    List<String> missing = new ArrayList<>();
    if (adminUsername.isBlank()) {
      missing.add("bootstrap.admin.username");
    }
    if (adminEmail.isBlank()) {
      missing.add("bootstrap.admin.email");
    }
    if (adminPassword.isBlank()) {
      missing.add("bootstrap.admin.password");
    }
    if (!missing.isEmpty()) {
      log.warn(
          "app.admin.* not fully configured ({} missing): no admin created",
          String.join(", ", missing));
      return;
    }
    if (appUserRepository.existsByRole(Role.ADMIN)) {
      log.info("App User already exists , skipping creation");
      return;
    }
    accountService.addAccount(adminUsername, adminEmail, adminPassword, Role.ADMIN);
    log.info("Admin account '{}' created", adminUsername);
  }
}
