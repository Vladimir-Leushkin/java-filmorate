package ru.yandex.practicum.javafilmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();
    private List<Director> directors = new ArrayList<>();
    private List<Mark> marks = new ArrayList<>();
}
