
package ru.yandex.practicum.javafilmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Integer id) {
        return filmService.findFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeFilmByUserId(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLikeFilmByUserId(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilmByUserId(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLikeFilmByUserId(id, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable Integer id) {
        filmService.deleteFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> findAllByLikes(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.findAllByLikes(count);
    }

    @GetMapping("/common")
    public List<Film> findCommonByUser(@RequestParam Integer userId, Integer friendId) {
        return filmService.findCommonByUser(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsListDirector(@PathVariable Integer directorId,
                                           @RequestParam(defaultValue = "year", required = false) String sortBy) {
        return filmService.getFilmsListDirector(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(@RequestParam String query, String by) {
        return filmService.searchFilms(query, by);
    }
}

