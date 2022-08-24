package ru.yandex.practicum.javafilmorate.dao;

import ru.yandex.practicum.javafilmorate.enums.EventType;
import ru.yandex.practicum.javafilmorate.enums.OperationType;
import ru.yandex.practicum.javafilmorate.model.Event;

import java.util.List;

public interface EventStorage {
    void addEvent(int userId, int entityId, EventType eventType, OperationType operation);

    List<Event> getEventForUser(int id);
}
