package ru.job4j.urlshortcut.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.urlshortcut.models.Site;

public interface SiteRepository extends CrudRepository<Site, Integer> {
    Site findByLogin(String login);
}
