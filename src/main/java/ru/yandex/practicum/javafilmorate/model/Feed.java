package ru.yandex.practicum.javafilmorate.model;

import lombok.*;
import ru.yandex.practicum.javafilmorate.enums.FeedType;
import ru.yandex.practicum.javafilmorate.enums.OperationType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Feed {
    private Integer id;
    private LocalDateTime timestamp;
    private Integer userId;
    private FeedType feedType;
    private OperationType operation;
    private Integer entityId;
}
