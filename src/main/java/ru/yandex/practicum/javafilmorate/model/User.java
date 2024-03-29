package ru.yandex.practicum.javafilmorate.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class User {
    private Integer id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;

}
