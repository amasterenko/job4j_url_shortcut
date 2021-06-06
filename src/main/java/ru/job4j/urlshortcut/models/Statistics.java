package ru.job4j.urlshortcut.models;

import javax.persistence.*;
import java.util.Objects;
/**
 * Class represents statistics entity.
 *
 * @author AndrewMs
 * @version 1.0
 */
@Entity
@Table(name = "urlstat")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int count;

    public Statistics() {
    }

    public Statistics(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public void incrementCount() {
        ++this.count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Statistics statistics = (Statistics) o;
        return id == statistics.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
