package ru.yandex.practicum.javafilmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.dao.FilmStorage;
import ru.yandex.practicum.javafilmorate.enums.EventType;
import ru.yandex.practicum.javafilmorate.enums.OperationType;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final EventService eventService;
    private static final LocalDate FIRST_RELEASE = LocalDate.of(1895, 12, 28);
    private static final int MAX_DESCRIPTION = 200;
    private static final int MIN_DURATION = 0;

    public List<Film> findAll() {
        return filmStorage.getFilmsList();
    }

    public Film createFilm(Film film) {
        validateFilm(film);
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        checkFilmById(film.getId());
        filmStorage.update(film);
        return findFilmById(film.getId());
    }

    public Film findFilmById(Integer id) {
        checkFilmById(id);
        return filmStorage.findFilmById(id);
    }

    public void addLikeFilmByUserId(Integer id, Integer userId, Integer mark) {
        checkFilmById(id);
        userService.checkUserById(userId);
        filmStorage.addLike(id, userId, mark);
        eventService.addEvent(userId, id, EventType.LIKE, OperationType.ADD);
    }

    public void deleteLikeFilmByUserId(Integer id, Integer userId) {
        checkFilmById(id);
        userService.checkUserById(userId);
        filmStorage.deleteLike(id, userId);
        eventService.addEvent(userId, id, EventType.LIKE, OperationType.REMOVE);
    }

    public List<Film> findAllByLikes(Integer count) {
        return filmStorage.getPopularFilmsList(count);
    }

    public List<Film> findCommonByUser(Integer userId, Integer friendId) {
        return filmStorage.findCommonByUser(userId, friendId);
    }

    public void deleteFilmById(Integer id) {
        filmStorage.deleteFilm(id);
    }

    public List<Film> getFilmsListDirector(Integer directorId, String sortBy) {
        List<Film> films = filmStorage.getFilmsListDirector(directorId, sortBy);
        if (films.size() == 0) {
            throw new NotFoundException("Directors not found");
        } else return films;
    }

    public List<Film> recommendations(Integer id) {
        userService.checkUserById(id);
        return filmStorage.recommendations(id);
    }

    private void checkFilmById(Integer id) {
        List<Film> films = filmStorage.getFilmsList();
        Map<Integer, Film> filmsMap = new HashMap<>();
        for (Film film : films) {
            filmsMap.put(film.getId(), film);
        }
        if (!filmsMap.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
    }

    private void validateFilm(Film film) {
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
    }

    public List<Film> searchFilms(String query, String byConditions) {
        return filmStorage.searchFilms(query, byConditions);
    }
}

