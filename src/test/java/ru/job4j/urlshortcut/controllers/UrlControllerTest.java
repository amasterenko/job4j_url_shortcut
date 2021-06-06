package ru.job4j.urlshortcut.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.urlshortcut.ShortcutApplication;
import ru.job4j.urlshortcut.CustomUser;
import ru.job4j.urlshortcut.service.UrlService;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShortcutApplication.class)
@AutoConfigureMockMvc
public class UrlControllerTest {

    @MockBean
    private UrlService urlService;

    @MockBean
    private CustomUser customUser;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenConvertThenReturn200AndJson() throws Exception {
        Map<String, String> result = Map.of("code", "shortcut");
        when(urlService.convert(any(), any())).thenReturn(result);
        this.mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"urlToConvert\"}")
                .with(user(customUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.code", is(result.get("code"))));
    }

    @Test
    public void whenConvertAndBadParametersThenReturn400() throws Exception {
        Map<String, String> result = Map.of("code", "shortcut");
        when(urlService.convert(any(), any())).thenReturn(result);
        this.mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"uR\":\"urlToConvert\"}")
                        .with(user(customUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenConvertAndUrlExistsThenReturn422() throws Exception {
        Map<String, String> result = Map.of("code", "");
        when(urlService.convert(any(), any())).thenReturn(result);
        this.mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"urlToConvert\"}")
                        .with(user(customUser)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void whenRedirectThenReturn302AndRedirectUrl() throws Exception {
        String resultUrl = "urlToRedirect";
        String code = "shortcut";
        when(urlService.getUrl(code)).thenReturn(resultUrl);
        this.mockMvc.perform(get("/redirect/" + code))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, resultUrl));
    }

    @Test
    public void whenRedirectAndNoUrlThenReturn404() throws Exception {
        String resultUrl = "";
        String code = "shortcut";
        when(urlService.getUrl(code)).thenReturn(resultUrl);
        this.mockMvc.perform(get("/redirect/" + code))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenStatThenReturn200AndJson() throws Exception {
        Map<String, String> result = Map.of("url", "url1", "total", "100");
        when(urlService.getStat(any())).thenReturn(List.of(result));
        this.mockMvc.perform(get("/statistic")
                        .with(user(customUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].length()", is(2)))
                .andExpect(jsonPath("$[0].url", is(result.get("url"))))
                .andExpect(jsonPath("$[0].total", is(result.get("total"))));
    }

}