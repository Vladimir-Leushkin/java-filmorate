package ru.yandex.practicum.javafilmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Feed;

import java.util.Collection;

@Service
public class FeedService {
    private final UserService userService;

    @Autowired
    public FeedService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Feed> getFeedForUser(int id) {
        if (userService.findUserById(id) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        //return eventStorage.getFeedForUser(id);
        return null;
    }
}
