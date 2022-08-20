package ru.yandex.practicum.javafilmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.enums.EventType;
import ru.yandex.practicum.javafilmorate.enums.OperationType;
import ru.yandex.practicum.javafilmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component
public class EventDBStorage implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    public EventDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addEvent(int userId, int entityId, EventType eventType, OperationType operation) {

    }

    @Override
    public Collection<Event> getEventForUser(int id) {
        return jdbcTemplate.query("SELECT * FROM event WHERE id=?", (rs, rowNum) -> makeEvent(rs), id);
    }

    private Event makeEvent(ResultSet rs) throws SQLException {
        return Event.builder().
                id(rs.getInt("id"))
                .timestamp(rs.getTimestamp("event_time").toLocalDateTime())
                .userId(rs.getInt("user_id"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(OperationType.valueOf(rs.getString("operation")))
                .entityId(rs.getInt("entity_id"))
                .build();
    }

}
