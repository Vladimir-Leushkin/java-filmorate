package ru.yandex.practicum.javafilmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int setId = 1;
    private static final LocalDate FIRST_RELEASE = LocalDate.of(1895, 12, 28);
    private static final int MAX_DESCRIPTION = 200;
    private static final int MIN_DURATION = 0;

    public static int getSetId() {
        return setId++;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        final List<Film> filmsList = new ArrayList<>(films.values());
        log.debug("Текущее количество фильмов {}", films.size());
        return filmsList;
    }

    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) {
        if (validationFilm(film)) {
            film.setId(getSetId());
            log.debug("Добавлен фильм: {}", film);
            films.put(film.getId(), film);
        }
        return film;

    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        if (validationFilm(film)) {
            if (!films.containsKey(film.getId())) {
                throw new ValidationException("Фильм не найден");
            }
            log.debug("Обновлена информация о фильме: {}", film);
            films.put(film.getId(), film);
        }
        return film;
    }

    private boolean validationFilm(Film film) {
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
