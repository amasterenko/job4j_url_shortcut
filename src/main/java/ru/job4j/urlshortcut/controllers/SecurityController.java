package ru.job4j.urlshortcut.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.urlshortcut.exceptions.BadRequestException;
import ru.job4j.urlshortcut.service.AuthService;
import ru.job4j.urlshortcut.service.SiteService;

import java.util.Map;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
/**
 * Class handles registration and authorization POST-requests.
 *
 * @author AndrewMs
 * @version 1.0
 */

@RestController
public class SecurityController {
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    private final SiteService siteService;

    private final AuthService authService;

    public SecurityController(SiteService siteService, AuthService authService) {
        this.siteService = siteService;
        this.authService = authService;
    }

    /**
     * Performs registration of the site.
     * Provides alpha-numeric login/password.
     *
     * @param reqBody JSON {"site":"SITE_NAME"}
     * @return JSON {"registration":"true/false", "login":"LOGIN", "password":"PASSWORD"}
     */
    @PostMapping("/registration")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> reqBody) {
        if (!reqBody.containsKey("site")) {
            throw new BadRequestException();
        }
        return new ResponseEntity<>(
                siteService.register(reqBody.get("site")),
                HttpStatus.OK
        );
    }

    /**
     * Performs authentication/authorization of the site.
     *
     * @param reqBody JSON {"login":"LOGIN", "password":"PASSWORD"}
     * @return HTTP-Header "Authorization: Bearer TOKEN"
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Map<String, String> reqBody) {
        if (!reqBody.containsKey("login") || !reqBody.containsKey("password")) {
            throw new BadRequestException();
        }
        String token = authService.auth(reqBody.get("login"), reqBody.get("password"));
        return token.isEmpty() ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
                : ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token
                ).build();
    }
}
