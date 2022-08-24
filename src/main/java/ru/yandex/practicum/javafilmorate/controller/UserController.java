package ru.yandex.practicum.javafilmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.service.EventService;
import ru.yandex.practicum.javafilmorate.service.FilmService;
import ru.yandex.practicum.javafilmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping
public class UserController {
    private final UserService userService;
    private final EventService eventService;
    private final FilmService filmService;


    public UserController(UserService userService, EventService eventService, FilmService filmService) {
        this.userService = userService;
        this.eventService = eventService;
        this.filmService = filmService;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable Integer id) {
        return userService.findUserById(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> findAllFriends(@PathVariable Integer id) {
        return userService.findAllFriends(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.findCommonFriends(id, otherId);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/users/{id}/feed")
    @ResponseStatus(HttpStatus.OK)
    public List<Event> getEventForUser(@PathVariable("id") Integer id) {
        return eventService.getEventForUser(id);
    }

    @GetMapping("/users/{id}/recommendations")
    public List<Film> recommendations(@PathVariable Integer id) {
        return filmService.recommendations(id);
    }

}
