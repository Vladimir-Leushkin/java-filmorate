package ru.yandex.practicum.javafilmorate.storage.user;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsersList();

    User addUser(User user);

    User findUserById(Integer id);

    void addFriend(Integer id, Integer friendId);

    void deleteFriend(Integer id, Integer friendId);

    List<Integer> findAllFriendsId(Integer id);

    List<User> findAllFriends(Integer id);

    List<Integer> findCommonFriendsId(Integer id, Integer otherId);

    List<User> findCommonFriends(Integer id, Integer otherId);

    void update(User user);
}
