package ru.yandex.practicum.javafilmorate.dao;

import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Mark;

import java.util.List;

public interface MarkStorage {
    List<Mark> getMarkList();

    void setFilmMarks(Film film);

    List<Mark> loadFilmMark(Integer filmId);
}
