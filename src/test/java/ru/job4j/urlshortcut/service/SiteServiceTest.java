package ru.job4j.urlshortcut.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.job4j.urlshortcut.models.Site;
import ru.job4j.urlshortcut.repositories.SiteRepository;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = SiteService.class)
class SiteServiceTest {
    @MockBean
    private SiteRepository sites;
    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    public void whenRegisterSuccessful() {
        SiteService service = new SiteService(sites, encoder);
        Map<String, Object> result = service.register("site_name");
        assertThat(result.size(), is(3));
        assertThat(result.get("registration"), is(true));
        assertThat(result.get("login").toString().length(), greaterThan(0));
        assertThat(result.get("password").toString().length(), greaterThan(0));
    }

    @Test
    public void whenRegisterFailed() {
        Mockito.when(sites.save(any())).thenThrow(DataIntegrityViolationException.class);
        SiteService service = new SiteService(sites, encoder);
        Map<String, Object> result = service.register("site_name");
        assertThat(result.size(), is(1));
        assertThat(result.get("registration"), is(false));
    }

    @Test
    public void whenLoadUserByUsername() {
        String login = "login";
        Mockito.when(sites.findByLogin(login)).thenReturn(new Site("sitename", login, "pwd"));
        SiteService service = new SiteService(sites, encoder);
        UserDetails userDetails = service.loadUserByUsername(login);
        assertThat(userDetails.getUsername(), is(login));
    }
    }