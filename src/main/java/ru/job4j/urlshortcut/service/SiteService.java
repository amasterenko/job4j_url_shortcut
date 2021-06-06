package ru.job4j.urlshortcut.service;

import static org.apache.commons.lang3.RandomStringUtils.random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.models.Site;
import ru.job4j.urlshortcut.repositories.SiteRepository;
import ru.job4j.urlshortcut.CustomUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *  Class represents a service layer.
 *  It is used for registration of the sites
 *  and for retrieving user(site)-related data for Spring Http Security.
 *
 *@author AndrewMs
 *@version 1.0
 */
@Service
public class SiteService implements UserDetailsService {
    private static final Logger LOG = LoggerFactory.getLogger(SiteService.class);
    @Value("${login.length}")
    private int loginLength = 6;
    @Value("${password.length}")
    private int passwordLength = 8;
    private final SiteRepository sites;
    private final BCryptPasswordEncoder encoder;

    public SiteService(SiteRepository sites, BCryptPasswordEncoder encoder) {
        this.sites = sites;
        this.encoder = encoder;
    }

    public Map<String, Object> register(String siteName) {
        Map<String, Object> res = new HashMap<>();
        res.put("registration", false);
        String login = random(loginLength / 2, true, false)
                + random(loginLength - loginLength / 2, false, true);
        String password = random(passwordLength, true, true);
        try {
            sites.save(new Site(siteName, login, encoder.encode(password)));
        } catch (Exception e) {
            LOG.error("exception:", e);
            return res;
        }
        res.put("registration", true);
        res.put("login", login);
        res.put("password", password);
        return res;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Site user = sites.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException(login);
        }
        return new CustomUser(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                List.of()
        );
    }
}
