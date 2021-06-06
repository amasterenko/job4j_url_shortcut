package ru.job4j.urlshortcut.controllers;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.urlshortcut.ShortcutApplication;
import ru.job4j.urlshortcut.service.AuthService;
import ru.job4j.urlshortcut.service.SiteService;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SecurityController.class)
public class SecurityControllerTest {
    @MockBean
    private SiteService siteService;

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser()
    public void whenRegisterThenReturn200AndJson() throws Exception {
        String siteName = "siteToRegister";
        Map<String, Object> result = Map.of("registration", true,
                "login", "newLogin", "password", "newPassword");
        when(siteService.register(siteName)).thenReturn(result);
        this.mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"site\":\"" + siteName + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$.registration", is(result.get("registration"))))
                .andExpect(jsonPath("$.login", is(result.get("login"))))
                .andExpect(jsonPath("$.password", is(result.get("password"))));
    }

    @Test
    @WithMockUser
    public void whenRegisterAndBadParametersThenReturn400() throws Exception {
        String siteName = "siteToRegister";
        this.mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + siteName + "\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void whenLoginThenReturn200AndToken() throws Exception {
        String resultToken = "token";
        Map<String, String> input = Map.of("login", "login",
                "password", "password");
        when(authService.auth(input.get("login"), input.get("password"))).thenReturn(resultToken);
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(input).toString()))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer " + resultToken));
    }

    @Test
    @WithMockUser
    public void whenAuthFailedThenReturn401() throws Exception {
        String resultToken = "";
        Map<String, String> input = Map.of("login", "login",
                "password", "password");
        when(authService.auth(input.get("login"), input.get("password"))).thenReturn(resultToken);
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(input).toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void whenLoginAndBadParametersThenReturn400() throws Exception {
        String resultToken = "token";
        Map<String, String> input = Map.of("name", "login",
                "password", "password");
        when(authService.auth(input.get("login"), input.get("password"))).thenReturn(resultToken);
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(input).toString()))
                .andExpect(status().isBadRequest());
    }
}