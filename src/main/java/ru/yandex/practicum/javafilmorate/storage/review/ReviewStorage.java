package ru.yandex.practicum.javafilmorate.storage.review;

import ru.yandex.practicum.javafilmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review addReview(Review review);
    Review changeReview(Review review);
    void deleteReview(Integer id);
    List<Review> getAllReview();
    Review findReviewById(Integer id);
    List<Review> getAllReviewByIdFilm(Integer filmId, Integer count);
    void changeUseful(Integer id, Integer num);
}
