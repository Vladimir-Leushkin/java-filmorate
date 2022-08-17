package ru.yandex.practicum.javafilmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.enums.FeedType;
import ru.yandex.practicum.javafilmorate.enums.OperationType;
import ru.yandex.practicum.javafilmorate.model.Feed;

import java.util.Collection;

@Slf4j
@Component
public class FeedDBStorage implements FeedStorage{

    private final JdbcTemplate jdbcTemplate;

    public FeedDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFeed(int userId, int entityId, FeedType feedType, OperationType operation) {

    }

    @Override
    public Collection<Feed> getFeedForUser(int id) {
        return null;
    }
}
