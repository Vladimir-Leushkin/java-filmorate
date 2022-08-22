package ru.yandex.practicum.javafilmorate.storage.review;

import ru.yandex.practicum.javafilmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    List<Review> getAllReview();
    Review addReview(Review review);
    void deleteReview(Integer id);
    Review changeReview(Review review);
    Review findReviewById(Integer id);
}
