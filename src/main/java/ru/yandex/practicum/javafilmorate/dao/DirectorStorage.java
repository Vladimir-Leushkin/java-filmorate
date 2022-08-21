package ru.yandex.practicum.javafilmorate.dao;

import ru.yandex.practicum.javafilmorate.model.Director;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.List;

public interface DirectorStorage {

    List<Director> getDirectorList();

    Director findDirectorById(Integer id);

    public Director addDirector(Director director);

    public Integer deleteDirectorById(Integer id);

    public Integer updateDirector(Director director);

    public List<Director> findDirectorsByFilmId(Integer filmId);

    public void addFilmDirectors(Film film);

}


