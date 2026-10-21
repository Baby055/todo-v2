package com.sombiniaina.project.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sombiniaina.project.endpoint.rest.controller.AuthController;
import com.sombiniaina.project.endpoint.rest.controller.TodoController;
import com.sombiniaina.project.service.AccountService;
import com.sombiniaina.project.service.TodoService;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {TodoController.class, AuthController.class})
@Import(SecurityConf.class)
class SecurityIT {

  @Autowired private MockMvc mockMvc;

  @MockBean private TodoService todoService;
  @MockBean private AccountService accountService;

  @Test
  void anonymous_isRedirectedToLoginOnTasks() throws Exception {
    mockMvc
        .perform(get("/tasks"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/login"));
  }

  @Test
  void anonymous_isRedirectedToLoginOnAdminDashboard() throws Exception {
    mockMvc
        .perform(get("/admin/dashboard"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/login"));
  }

  @Test
  @WithMockUser(roles = "USER")
  void user_isForbiddenOnAdminDashboard() throws Exception {
    mockMvc.perform(get("/admin/dashboard")).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "USER")
  void postWithoutCsrf_isForbidden() throws Exception {
    mockMvc
        .perform(post("/tasks/create").param("title", "t").param("description", "d"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "USER")
  void postWithCsrf_passesCsrfFilter() throws Exception {
    mockMvc
        .perform(post("/tasks/create").with(csrf()).param("title", "t").param("description", "d"))
        .andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(roles = "USER")
  void user_cannotDeleteAdminTaskEvenWithCsrf() throws Exception {
    mockMvc
        .perform(post("/admin/tasks/{id}/delete", UUID.randomUUID()).with(csrf()))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "USER")
  @Disabled("templates manquants")
  void user_isNotForbiddenOnTasks() throws Exception {
    mockMvc.perform(get("/tasks")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @Disabled("templates manquants")
  void admin_canAccessAdminDashboard() throws Exception {
    mockMvc.perform(get("/admin/dashboard")).andExpect(status().isOk());
  }

  @Test
  @Disabled("templates manquants")
  void anonymous_canAccessRegisterAndLoginWithoutRedirect() throws Exception {
    mockMvc.perform(get("/register")).andExpect(status().isOk());
    mockMvc.perform(get("/login")).andExpect(status().isOk());
  }
}
