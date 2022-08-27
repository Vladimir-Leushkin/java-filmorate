package ru.yandex.practicum.javafilmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.dao.EventStorage;
import ru.yandex.practicum.javafilmorate.enums.EventType;
import ru.yandex.practicum.javafilmorate.enums.OperationType;
import ru.yandex.practicum.javafilmorate.model.Event;

import java.util.List;

@Service
public class EventService {
    private final EventStorage eventStorage;

    @Autowired
    public EventService(EventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    public List<Event> getEventForUser(int id) {
        return eventStorage.getEventForUser(id);
    }

    public void addEvent(int userId, int entityId, EventType eventType, OperationType operation) {
        eventStorage.addEvent(userId, entityId, eventType, operation);
    }
}
