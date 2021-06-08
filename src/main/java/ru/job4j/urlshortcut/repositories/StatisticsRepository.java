package ru.job4j.urlshortcut.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.urlshortcut.models.Statistics;

public interface StatisticsRepository extends CrudRepository<Statistics, Integer> {
    @Modifying
    @Query("update Statistics s set s.count = s.count + 1 where s.id = ?1")
    void incrementCounterById(int id);
}
