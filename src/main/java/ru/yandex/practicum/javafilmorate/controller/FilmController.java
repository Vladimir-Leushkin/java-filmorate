package ru.yandex.practicum.javafilmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable Integer id) {
        return filmService.findFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLikeFilmByUserId(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLikeFilmByUserId(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFilmByUserId(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLikeFilmByUserId(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> findAllByLikes(@RequestParam(defaultValue = "10", required = false)
                                             Integer count) {
        return filmService.findAllByLikes(count);
    }

}
