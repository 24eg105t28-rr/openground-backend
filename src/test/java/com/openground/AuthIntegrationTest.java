package com.openground;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openground.dto.AuthRequest;
import com.openground.dto.RegisterRequest;
import com.openground.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterAndLoginWorkflow() throws Exception {
        RegisterRequest registerReq = new RegisterRequest(
                "Captain Dhoni",
                "dhoni.captain@cricket.org",
                "helicopter77",
                "+91 9988776655",
                Role.CUSTOMER
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.user.email").value("dhoni.captain@cricket.org"))
                .andExpect(jsonPath("$.user.fullName").value("Captain Dhoni"));

        AuthRequest loginReq = new AuthRequest(
                "dhoni.captain@cricket.org",
                "helicopter77"
        );

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.user.email").value("dhoni.captain@cricket.org"));
    }
}
