package ru.job4j.urlshortcut.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.urlshortcut.models.Statistics;

public interface StatisticsRepository extends CrudRepository<Statistics, Integer> {
}
