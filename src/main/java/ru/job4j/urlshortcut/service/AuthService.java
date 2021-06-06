package ru.job4j.urlshortcut.service;

import com.auth0.jwt.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
/**
 * Class is a service layer for performing authorizations of the sites(users).
 *
 *@author AndrewMs
 *@version 1.0
 */
@Service
public class AuthService {
    public static final String SECRET = "SecretKeyUrlShortcut";
    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);
    @Value("#{new Long('${jwt.ttl}')}")
    private long expirationTime = 864_000_000; /* 10 days */

    private final AuthenticationManager auth;

    public AuthService(AuthenticationManager auth) {
        this.auth = auth;
    }

    public String auth(String login, String password) {
        String token;
        try {
            Authentication authenticate = auth
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    login, password
                            )
                    );
            token = JWT.create()
                    .withSubject(((User) authenticate.getPrincipal()).getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                    .sign(HMAC512(SECRET.getBytes()));
        } catch (BadCredentialsException e) {
            LOG.error("exception", e);
            return "";
        }
        return token;
    }
}
