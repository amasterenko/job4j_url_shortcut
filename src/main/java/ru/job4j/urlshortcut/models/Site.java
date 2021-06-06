package ru.job4j.urlshortcut.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/**
 * Class represents site entity. It encapsulates site credentials and acts as system user.
 *
 * @author AndrewMs
 * @version 1.0
 */
@Entity
@Table(name = "sites")
public class Site {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String login;
    private String password;
    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Url> urls = new HashSet<>();

    public Site(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public Site(int id) {
        this.id = id;
    }

    public Site() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Url> getUrls() {
        return urls;
    }

    public void setUrls(Set<Url> urls) {
        this.urls = urls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Site site = (Site) o;
        return id == site.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
