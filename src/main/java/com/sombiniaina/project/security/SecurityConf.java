package com.sombiniaina.project.security;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConf {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(withDefaults())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/ping", "/health/**", "/register", "/login", "/error")
                    .permitAll()
                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN")
                    .requestMatchers("/", "/css/**", "/js/**")
                    .permitAll()
                    .requestMatchers("/tasks/**")
                    .hasAnyRole("ADMIN", "USER")
                    .anyRequest()
                    .authenticated())
        .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/tasks", true).permitAll())
        .logout(logout -> logout.logoutSuccessUrl("/"));
    return http.build();
  }
}
