package ru.yandex.practicum.javafilmorate.storage.film;

import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getFilmsList();

    Film addFilm(Film film);

    Film findFilmById(Integer id);

    void update(Film film);

    List<Film> getPopularFilmsList(Integer count);

    void addLike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);

    void deleteFilm(Integer id);

    public List<Film> getFilmsListDirector(Integer directorId, String sortBy);
}
