package ru.yandex.practicum.javafilmorate.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Review {
    private Integer reviewId;
    @NonNull
    private String content;
    @NonNull
    private Boolean isPositive;
    @NonNull
    private Integer userId;
    @NonNull
    private Integer filmId;
    private Integer useful;
}
