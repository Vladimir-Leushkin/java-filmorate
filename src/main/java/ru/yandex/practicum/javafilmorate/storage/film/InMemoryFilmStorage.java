package ru.yandex.practicum.javafilmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    public Map<Integer, Film> getFilms() {
        return films;
    }
}
