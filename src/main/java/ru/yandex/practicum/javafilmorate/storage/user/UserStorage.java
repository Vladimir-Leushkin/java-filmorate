package ru.yandex.practicum.javafilmorate.storage.user;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.Map;

public interface UserStorage {
    Map<Integer, User> getUsers();
}
