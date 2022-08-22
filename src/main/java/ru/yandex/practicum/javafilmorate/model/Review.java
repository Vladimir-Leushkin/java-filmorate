package ru.yandex.practicum.javafilmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
public class Review {
    private int idReview;
    @NotBlank
    private final String content;
    @NotNull
    private final Boolean isPositive;
    @NotNull
    private final Integer userId;
    @NotNull
    private final Integer filmId;
    private Integer useful;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id && Objects.equals(content, review.content) && Objects.equals(isPositive, review.isPositive) && Objects.equals(userId, review.userId) && Objects.equals(filmId, review.filmId) && Objects.equals(useful, review.useful);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, isPositive, userId, filmId, useful);
    }
}
