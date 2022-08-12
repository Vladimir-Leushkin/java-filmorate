
package ru.yandex.practicum.javafilmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getUsersList() {
        final List<User> usersList = new ArrayList<>(users.values());
        return usersList;
    }

    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
    }

    @Override
    public List<Integer> findAllFriendsId(Integer id) {
        return null;
    }

    @Override
    public List<User> findAllFriends(Integer id) {
        List<Integer> userFriendsId = findAllFriendsId(id);
        List<User> usersFriends = new ArrayList<>();
        if (userFriendsId.isEmpty()) {
            return usersFriends;
        }
        for (Integer userid : userFriendsId) {
            usersFriends.add(users.get(userid));
        }
        return usersFriends;
    }

    @Override
    public List<Integer> findCommonFriendsId(Integer id, Integer otherId) {
        List<Integer> commonFriendsId = new ArrayList<>();
        if (findAllFriendsId(id) == null ||
                findAllFriendsId(otherId) == null) {
            return commonFriendsId;
        }
        commonFriendsId.addAll(findAllFriendsId(id));
        commonFriendsId.retainAll(findAllFriendsId(otherId));
        return commonFriendsId;
    }

    @Override
    public List<User> findCommonFriends(Integer id, Integer otherId) {
        List<Integer> commonFriendsId = findCommonFriendsId(id, otherId);
        List<User> commonFriends = new ArrayList<>();
        if (commonFriendsId.isEmpty()) {
            return commonFriends;
        }
        for (Integer userid : commonFriendsId) {
            commonFriends.add(users.get(userid));
        }
        return commonFriends;
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }
}

