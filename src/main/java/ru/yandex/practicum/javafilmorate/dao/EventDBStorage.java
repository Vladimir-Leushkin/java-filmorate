package ru.yandex.practicum.javafilmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.enums.EventType;
import ru.yandex.practicum.javafilmorate.enums.OperationType;
import ru.yandex.practicum.javafilmorate.model.Event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class EventDBStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addEvent(int userId, int entityId, EventType eventType, OperationType operation) {
        String sqlQuery = "INSERT INTO event(user_id, event_type, operation, entity_id)" +
                " VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setInt(1, userId);
            stmt.setString(2, eventType.name());
            stmt.setString(3, operation.name());
            stmt.setInt(4, entityId);
            return stmt;
        }, keyHolder);
    }

    @Override
    public List<Event> getEventForUser(int id) {
        return jdbcTemplate.query("SELECT * FROM event WHERE user_id=?", this::makeEvent, id);
    }

    private Event makeEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getInt("id"))
                .timestamp(rs.getTimestamp("event_time").getTime())
                .userId(rs.getInt("user_id"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(OperationType.valueOf(rs.getString("operation")))
                .entityId(rs.getInt("entity_id"))
                .build();
    }
}
