package ru.yandex.practicum.javafilmorate.model;

import lombok.*;
import ru.yandex.practicum.javafilmorate.enums.EventType;
import ru.yandex.practicum.javafilmorate.enums.OperationType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = "id")
public class Event {
    private Integer eventId;
    private Long timestamp;
    private Integer userId;
    private EventType eventType;
    private OperationType operation;
    private Integer entityId;
}
