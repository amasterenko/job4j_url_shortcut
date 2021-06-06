package ru.job4j.urlshortcut.models;

import javax.persistence.*;
import java.util.Objects;
/**
 * Class represents URL entity.
 *
 * @author AndrewMs
 * @version 1.0
 */
@Entity
@Table(name = "urls")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String shortcut;
    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", foreignKey = @ForeignKey(name = "urls_site_id_fkey"))
    private Site site;
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "urlstat_id", foreignKey = @ForeignKey(name = "urls_urlstat_id_fkey"))
    private Statistics stat;

    public Url(String shortcut, String url, Site site, Statistics stat) {
        this.shortcut = shortcut;
        this.url = url;
        this.site = site;
        this.stat = stat;
    }

    public Url() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site owner) {
        this.site = owner;
    }

    public Statistics getStat() {
        return stat;
    }

    public void setStat(Statistics stat) {
        this.stat = stat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Url url = (Url) o;
        return id == url.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
