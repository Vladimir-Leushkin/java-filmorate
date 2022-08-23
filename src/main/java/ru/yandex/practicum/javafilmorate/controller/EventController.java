package ru.yandex.practicum.javafilmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.service.EventService;

import java.util.Collection;

@RestController
@Slf4j

@RequiredArgsConstructor
public class EventController {
//    private final EventService eventService;
//
//    @GetMapping("/{id}/feed")
//    public Collection<Event> getEventForUser(@PathVariable("id") Integer id) {
//        return eventService.getEventForUser(id);
//    }
}
