package com.rspl.onboarding;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HrControllerTest {

  @Autowired
  MockMvc mvc;

  @Test
  @WithMockUser(username = "testuser", roles = {"ADMIN"})
  void team_ok() throws Exception {
    mvc.perform(get("/api/hr/team"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data").isArray());
  }

  // @Test
  // @WithMockUser(username = "testuser", roles = {"ADMIN"})
  void candidates_ok() throws Exception {
    mvc.perform(get("/api/hr/candidates").param("page", "0").param("size", "5"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.content").isArray());
  }

  // @Test
  // @WithMockUser(username = "testuser", roles = {"ADMIN"})
  void initiate_ok() throws Exception {
    String body = "{" +
        "\"employeeName\":\"Test User\"," +
        "\"aadhaarNo\":\"123456789012\"," +
        "\"emailId\":\"test.user." + System.currentTimeMillis() + "@example.com\"," +
        "\"mobileNo\":\"9999999999\"," +
        "\"designation\":\"Intern\"," +
        "\"assignedHRId\":1," +
        "\"initiatedBy\":\"junit\"," +
        "\"sendLinkImmediately\":false" +
        "}";

    mvc.perform(post("/api/hr/candidates/initiate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.employeeName").value("Test User"));
  }
}
