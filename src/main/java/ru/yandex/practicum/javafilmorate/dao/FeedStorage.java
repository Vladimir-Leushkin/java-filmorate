package ru.yandex.practicum.javafilmorate.dao;

import ru.yandex.practicum.javafilmorate.enums.FeedType;
import ru.yandex.practicum.javafilmorate.enums.OperationType;
import ru.yandex.practicum.javafilmorate.model.Feed;

import java.util.Collection;

public interface FeedStorage {
    void addFeed(int userId, int entityId, FeedType feedType, OperationType operation);

    Collection<Feed> getFeedForUser(int id);
}
