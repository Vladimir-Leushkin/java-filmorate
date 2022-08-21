package ru.yandex.practicum.javafilmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Director;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.service.DirectorService;
import ru.yandex.practicum.javafilmorate.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping
public class DirectorController {
    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping("/directors")
    public List<Director> findAll() {
        return directorService.findAllDirectors();
    }

    @PostMapping(value = "/directors")
    public Director addDirector(@RequestBody Director director) {
        return directorService.addDirector(director);
    }

    @GetMapping("/directors/{id}")
    public Director findDirectorById(@PathVariable Integer id) {
        return directorService.findDirectorById(id);
    }

    @DeleteMapping("/directors/{id}")
    public void deleteDirectorById(@PathVariable Integer id) {
        directorService.deleteDirectorById(id);
    }

    @PutMapping(value = "/directors")
    public Director updateDirector(@RequestBody Director director) {
        return directorService.updateDirector(director);
    }
}
