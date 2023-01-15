package ru.yandex.practicum.javafilmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Mark {
    Integer filmId;
    Integer userId;
    Integer mark;
}
