package ru.yandex.practicum.javafilmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Director {
    private Integer id;
    private String name;
}
