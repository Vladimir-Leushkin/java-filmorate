package ru.yandex.practicum.javafilmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final LocalDate FIRST_RELEASE = LocalDate.of(1895, 12, 28);
    private static final int MAX_DESCRIPTION = 200;
    private static final int MIN_DURATION = 0;

    public List<Film> findAll() {
        final List<Film> filmsList = new ArrayList<>(filmStorage.getFilmsList());
        return filmsList;
    }

    public Film createFilm(Film film) {
        if (validateFilm(film)) {
            filmStorage.addFilm(film);
        }
        return film;
    }

    public Film updateFilm(Film film) {
        if (validateFilm(film)) {
            if (checkFilmById(film.getId())) {
                filmStorage.update(film);
            }
        }
        return findFilmById(film.getId());
    }

    public Film findFilmById(Integer id) {
        if (checkFilmById(id)) {
            return filmStorage.findFilmById(id);
        }
        return null;
    }

    public void addLikeFilmByUserId(Integer id, Integer userId) {
        if (checkFilmById(id)) {
            if (userService.checkUserById(userId)) {
                filmStorage.addLike(id, userId);
            }
        }
    }

    public void deleteLikeFilmByUserId(Integer id, Integer userId) {
        if (checkFilmById(id)) {
            if (userService.checkUserById(userId)) {
                filmStorage.deleteLike(id, userId);
            }
        }
    }

    public List<Film> findAllByLikes(Integer count) {
        return filmStorage.getPopularFilmsList(count);
    }



    private boolean checkFilmById(Integer id) {
        List<Film> films = filmStorage.getFilmsList();
        Map<Integer, Film> filmsMap = new HashMap<>();
        for (Film film: films){
            filmsMap.put(film.getId(), film);
        }
        if (!filmsMap.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        return true;
    }

    private boolean validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() >= MAX_DESCRIPTION) {
            throw new ValidationException("Описание превышает 200 символов");
        }
        if (film.getReleaseDate().isBefore(FIRST_RELEASE)) {
            throw new ValidationException("Дата релиза фильма не может быть до 28.12.1895");
        }
        if (film.getDuration() <= MIN_DURATION) {
            throw new ValidationException("Продолжительность фильма не может быть нулевой");
        }

        return true;
    }
}

