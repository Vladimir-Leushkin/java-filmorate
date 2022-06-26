package ru.yandex.practicum.javafilmorate.controller;

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
    public Film create(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.debug("Название фильма пустое {}", film.getName());
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() >= 200) {
            log.debug("Описание превышает 200 символов {}", film.getDescription().length());
            throw new ValidationException("Описание превышает 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.debug("Дата релиза фильма до 28.12.1895 {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза фильма не может быть до 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            log.debug("Продолжительность фильма {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма не может быть нулевой");
        }
        film.setId(getSetId());
        log.debug("Добавлен фильм: {}", film);
        films.put(film.getId(), film);
        System.out.println(film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.debug("Название фильма пустое {}", film.getName());
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() >= 200) {
            log.debug("Описание превышает 200 символов {}", film.getDescription().length());
            throw new ValidationException("Описание превышает 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.debug("Дата релиза фильма до 28.12.1895 {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза фильма не может быть до 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            log.debug("Продолжительность фильма {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма не может быть нулевой");
        }
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм не найден");
        }
        log.debug("Обновлена информация о фильме: {}", film);
        films.put(film.getId(), film);
        return film;
    }

}
