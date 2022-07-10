package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private static int setId = 1;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public static int getSetId() {
        return setId++;
    }

    public List<User> findAll() {
        final List<User> usersList = new ArrayList<>(userStorage.getUsers().values());
        log.debug("Текущее количество пользователей {}", userStorage.getUsers().size());
        return usersList;
    }

    public User createUser(User user) {
        if (validateUser(user)) {
            user.setId(getSetId());
            if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            log.debug("Добавлен пользователь: {}", user);
            userStorage.getUsers().put(user.getId(), user);
        }
        return user;
    }

    public User findUserById(Integer id) {
        if (checkUserById(id)) {
            log.debug("Найден пользователь {}", userStorage.getUsers().get(id));
            return userStorage.getUsers().get(id);
        }
        return null;
    }

    public User updateUser(User user) {
        if (validateUser(user)) {
            if (checkUserById(user.getId())) {
                log.debug("Обновлена информация о пользователе: {}", user);
                userStorage.getUsers().put(user.getId(), user);
            }
        }
        return user;
    }

    public void addFriend(Integer id, Integer friendId) {
        if (checkUserById(id) && checkUserById(friendId)) {
            userStorage.getUsers().get(id).addFriend(friendId);
            userStorage.getUsers().get(friendId).addFriend(id);
            log.debug("Добавлены друзья: {},  {}", id, friendId);
        }
    }

    public void deleteFriend(Integer id, Integer userId) {
        if (checkUserById(id) && checkUserById(userId)) {
            userStorage.getUsers().get(id).getFriends().remove(userId);
            userStorage.getUsers().get(userId).getFriends().remove(id);
            log.debug("Удалены друзья: {},  {}", id, userId);
        }
    }

    public List<User> findAllFriends(Integer id) {
        if (checkUserById(id)) {
            List<User> usersList = new ArrayList<>(userStorage.getUsers().values());
            List<Integer> userFriendsId = new ArrayList<>(userStorage.getUsers().get(id)
                    .getFriends());
            log.debug("Найдены друзья: {}", userFriendsId);
            List<User> usersFriends = new ArrayList<>();
            for (User user : usersList) {
                for (Integer userid : userFriendsId) {
                    if (user.getId() == userid) {
                        usersFriends.add(user);
                    }
                }
            }
            return usersFriends;
        }
        return null;
    }

    public List<User> findCommonFriends(Integer id, Integer otherId) {
        if (checkUserById(id) && checkUserById(otherId)) {
            List<User> usersList = new ArrayList<>();
            List<Integer> commonFriendsId = new ArrayList<>();
            if (userStorage.getUsers().get(id).getFriends() == null ||
                    userStorage.getUsers().get(otherId).getFriends() == null) {
                return usersList;
            }
            usersList.addAll(userStorage.getUsers().values());
            commonFriendsId.addAll(userStorage.getUsers().get(id).getFriends());
            commonFriendsId.retainAll(userStorage.getUsers().get(otherId).getFriends());
            log.debug("Найдены общие друзья: {}", commonFriendsId);
            List<User> commonFriends = new ArrayList<>();
            for (User user : usersList) {
                for (Integer userid : commonFriendsId) {
                    if (user.getId() == userid) {
                        commonFriends.add(user);
                    }
                }
            }
            return commonFriends;
        }
        return null;
    }

    protected boolean checkUserById(Integer id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return true;
    }

    private boolean validateUser(User user) {
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
        return true;
    }
}
