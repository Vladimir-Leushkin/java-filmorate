package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static int setId = 1;
    private static final LocalDate FIRST_RELEASE = LocalDate.of(1895, 12, 28);
    private static final int MAX_DESCRIPTION = 200;
    private static final int MIN_DURATION = 0;

    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public static int getSetId() {
        return setId++;
    }

    public List<Film> findAll() {
        final List<Film> filmsList = new ArrayList<>(filmStorage.getFilms().values());
        log.debug("Текущее количество фильмов {}", filmStorage.getFilms().size());
        return filmsList;
    }

    public Film createFilm(Film film) {
        if (validateFilm(film)) {
            film.setId(getSetId());
            log.debug("Добавлен фильм: {}", film);
            filmStorage.getFilms().put(film.getId(), film);
        }
        return film;
    }

    public Film updateFilm(Film film) {
        if (validateFilm(film)) {
            if (checkFilmById(film.getId())) {
                log.debug("Обновлена информация о фильме: {}", film);
                filmStorage.getFilms().put(film.getId(), film);
            }
        }
        return film;
    }

    public Film findFilmById(Integer id) {
        if (checkFilmById(id)) {
            log.debug("Найден фильм: {}", filmStorage.getFilms().get(id));
            return filmStorage.getFilms().get(id);
        }
        return null;
    }

    public void addLikeFilmByUserId(Integer id, Integer userId) {
        if (checkFilmById(id)) {
            if (userService.checkUserById(userId)) {
                filmStorage.getFilms().get(id).getLikes().add(userId);
                log.debug("Фильму: {}, добавлен лайк от пользователя: {}", id, userId);
                log.debug("У фильма: {}, есть лайки : {}", id, filmStorage.getFilms().get(id).getLikes());
            }
        }
    }

    public void deleteLikeFilmByUserId(Integer id, Integer userId) {
        if (checkFilmById(id)) {
            if (userService.checkUserById(userId)) {
                filmStorage.getFilms().get(id).getLikes().remove(userId);
                log.debug("Фильму: {}, удален лайк от пользователя: {}", id, userId);
            }
        }
    }

    public List<Film> findAllByLikes(Integer count) {
        List<Film> filmsList = new ArrayList<>(filmStorage.getFilms().values());
        return filmsList.stream()
                .sorted((f0, f1) -> compare(f0, f1))
                .limit(count)
                .collect(Collectors.toList());
    }

    public int compare(Film f0, Film f1) {
        int result = f0.numberLikes().compareTo(f1.numberLikes());
        result = -1 * result;

        return result;
    }


    private boolean checkFilmById(Integer id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        return true;
    }

    private boolean validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.debug("Название фильма пустое {}", film.getName());
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() >= MAX_DESCRIPTION) {
            log.debug("Описание превышает 200 символов {}", film.getDescription().length());
            throw new ValidationException("Описание превышает 200 символов");
        }
        if (film.getReleaseDate().isBefore(FIRST_RELEASE)) {
            log.debug("Дата релиза фильма до 28.12.1895 {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза фильма не может быть до 28.12.1895");
        }
        if (film.getDuration() <= MIN_DURATION) {
            log.debug("Продолжительность фильма {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма не может быть нулевой");
        }
        return true;
    }
}
