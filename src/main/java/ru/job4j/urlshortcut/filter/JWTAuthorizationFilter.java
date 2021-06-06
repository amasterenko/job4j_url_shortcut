package ru.job4j.urlshortcut.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.job4j.urlshortcut.service.SiteService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.job4j.urlshortcut.controllers.SecurityController.*;
import static ru.job4j.urlshortcut.service.AuthService.*;
/**
 * Class filters HTTP-requests and performs token-based authorization of the users(sites).
 *
 * @author AndrewMs
 * @version 1.0
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final SiteService sites;

    public JWTAuthorizationFilter(AuthenticationManager authManager, SiteService sites) {
        super(authManager);
        this.sites = sites;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            /* parse the token. */
            String login = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();

            if (login != null) {
                UserDetails userDetails = sites.loadUserByUsername(login);
                return new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
            }
            return null;
        }
        return null;
    }
}