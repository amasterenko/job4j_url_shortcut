package ru.job4j.urlshortcut.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.urlshortcut.models.Site;
import ru.job4j.urlshortcut.models.Url;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends CrudRepository<Url, Integer> {
    Optional<Url> findByShortcut(String shortcut);

    @Query("select u from Url u join fetch u.stat where u.site = ?1")
    List<Url> findAllBySite(Site site);
}
