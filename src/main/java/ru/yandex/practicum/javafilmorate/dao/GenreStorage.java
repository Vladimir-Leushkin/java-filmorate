package ru.yandex.practicum.javafilmorate.dao;

import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getGenreList();

    Genre findGenreById(Integer id);

    void setFilmGenre(Film film);

    List<Genre> loadFilmGenre(Integer filmId);
}
