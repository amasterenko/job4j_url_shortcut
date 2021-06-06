package ru.job4j.urlshortcut.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = AuthService.class)
public class AuthServiceTest {

    @MockBean
    private AuthenticationManager auth;
    @MockBean
    private Authentication authenticate;

    @Test
    public void whenAuthSuccessful() {
        String login = "login";
        String password = "password";
        User user = new User(login, password, List.of());
        Mockito.when(auth.authenticate(any())).thenReturn(authenticate);
        Mockito.when((User) (authenticate.getPrincipal())).thenReturn(user);
        AuthService service = new AuthService(auth);
        String token = service.auth(login, password);
        assertThat(token.length(), greaterThan(0));
    }

    @Test
    public void whenAuthFailed() {
        String login = "login";
        String password = "password";
        Mockito.when(auth.authenticate(any())).thenThrow(BadCredentialsException.class);
        AuthService service = new AuthService(auth);
        String token = service.auth(login, password);
        assertThat(token.length(), is(0));
    }
}