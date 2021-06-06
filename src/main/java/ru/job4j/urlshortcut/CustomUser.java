package ru.job4j.urlshortcut;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 *  Class-helper used for encapsulating id of Site.
 *
 *  @author AndrewMs
 *  @version 1.0
 */
public class CustomUser extends User {
    private final int id;

    public CustomUser(int id,
                      String login,
                      String password,
                      Collection<? extends GrantedAuthority> authorities) {
        super(login, password, authorities);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
