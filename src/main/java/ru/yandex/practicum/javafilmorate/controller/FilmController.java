
package ru.yandex.practicum.javafilmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.service.FilmService;
import ru.yandex.practicum.javafilmorate.service.GenreService;
import ru.yandex.practicum.javafilmorate.service.MpaService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final GenreService genreService;
    private final MpaService mpaService;

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

    @DeleteMapping("/films/{id}")
    public void deleteFilmById(@PathVariable Integer id) {
        filmService.deleteFilmById(id);
    }

    @GetMapping("/films/popular")
    public List<Film> findAllByLikes(@RequestParam(defaultValue = "10", required = false)
                                     Integer count) {
        return filmService.findAllByLikes(count);
    }

    @GetMapping("/films/common")
    public List<Film> findCommonByUser(@RequestParam Integer userId, Integer friendId) {
        return filmService.findCommonByUser(userId, friendId);
    }

    @GetMapping("/genres")
    public List<Genre> findAllGenres() {
        return genreService.findAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre findGenreById(@PathVariable Integer id) {
        return genreService.findGenreById(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> findAllMpa() {
        return mpaService.findAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa findMpaById(@PathVariable Integer id) {
        return mpaService.findMpaById(id);
    }

    @GetMapping("/films/director/{directorId}")
    public List<Film> getFilmsListDirector(@PathVariable Integer directorId,
                                           @RequestParam(defaultValue = "year", required = false) String sortBy) {
        return filmService.getFilmsListDirector(directorId, sortBy);
    }

    @GetMapping("/films/search")
    public List<Film> searchFilms(@RequestParam String query, String by) {
        return filmService.searchFilms(query, by);
    }
}

