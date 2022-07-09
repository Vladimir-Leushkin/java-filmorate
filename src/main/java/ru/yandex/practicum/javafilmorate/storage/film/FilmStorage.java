package ru.yandex.practicum.javafilmorate.storage.film;

import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    Map<Integer, Film> getFilms();
}
