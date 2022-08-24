package ru.yandex.practicum.javafilmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.javafilmorate.enums.EventType;
import ru.yandex.practicum.javafilmorate.enums.OperationType;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final EventService eventService;

    public List<User> findAll() {
        final List<User> usersList = new ArrayList<>(userStorage.getUsersList());
        return usersList;
    }

    public User createUser(User user) {
        if (validateUser(user)) {
            if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            userStorage.addUser(user);
        }
        return user;
    }

    public User findUserById(Integer id) {
        if (checkUserById(id)) {
            return userStorage.findUserById(id);
        }
        return null;
    }

    public User updateUser(User user) {
        if (validateUser(user)) {
            if (checkUserById(user.getId())) {
                userStorage.update(user);
            }
        }
        return user;
    }

    public void addFriend(Integer id, Integer friendId) {
        if (checkUserById(id) && checkUserById(friendId)) {
            userStorage.addFriend(id, friendId);
            eventService.addEvent(id, friendId, EventType.FRIEND, OperationType.ADD);
        }
    }

    public void deleteFriend(Integer id, Integer friendId) {
        if (checkUserById(id) && checkUserById(friendId)) {
            userStorage.deleteFriend(id, friendId);
            eventService.addEvent(id, friendId, EventType.FRIEND, OperationType.REMOVE);
        }
    }

    public List<User> findAllFriends(Integer id) {
        if (checkUserById(id)) {
            return userStorage.findAllFriends(id);
        }
        return null;
    }

    public List<User> findCommonFriends(Integer id, Integer otherId) {
        if (checkUserById(id) && checkUserById(otherId)) {
            return userStorage.findCommonFriends(id, otherId);
        }
        return null;
    }

    public void deleteUserById(Integer id){
        if (checkUserById(id)) {
            userStorage.deleteUser(id);
        }
    }

    protected boolean checkUserById(Integer id) {
        List<User> users = userStorage.getUsersList();
        Map<Integer, User> usersMap = new HashMap<>();
        for (User user : users) {
            usersMap.put(user.getId(), user);
        }
        if (!usersMap.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return true;
    }

    private boolean validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank() ||
                !user.getEmail().contains("@")) {
            throw new ValidationException("Адрес электронной почты не может быть пустым");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть больше " + LocalDate.now());
        }
        return true;
    }
}
