package ru.yandex.practicum.javafilmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping
public class UserController {
    @Getter
    private final Map<Integer, User> users = new HashMap<>();

    private static int setId = 1;

    public static int getSetId() {
        return setId++;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        final List<User> usersList = new ArrayList<>(users.values());
        log.debug("Текущее количество пользователей {}", users.size());
        return usersList;
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank() ||
                !user.getEmail().contains("@")) {
            log.debug("Адрес электронной почты пустой {}", user.getEmail());
            throw new ValidationException("Адрес электронной почты не может быть пустым");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            log.debug("Логин пустой {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения превышает текущую {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть больше " + LocalDate.now());
        }
        user.setId(getSetId());
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("Добавлен пользователь: {}", user);
        users.put(user.getId(), user);
        return user;

    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank() ||
                !user.getEmail().contains("@")) {
            log.debug("Адрес электронной почты пустой {}", user.getEmail());
            throw new ValidationException("Адрес электронной почты не может быть пустым");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            log.debug("Логин пустой {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения превышает текущую {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть больше " + LocalDate.now());
        }
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь не найден");
        }
        log.debug("Обновлена информация о пользователе: {}", user);
        users.put(user.getId(), user);
        return user;
    }
}
