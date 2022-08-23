package ru.yandex.practicum.javafilmorate.storage.review;

import ru.yandex.practicum.javafilmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    List<Review> getAllReview();

    Review findReviewById(Integer id);

    List<Review> getAllReviewByIdFilm(Integer filmId, Integer count);

    Review addReview(Review review);

    Review changeReview(Review review);

    void deleteReview(Integer id);

    void addLikeDislikeReview(Integer idReview, Integer userId, Integer num);

    void changeLikeDislikeReview(Integer idReview, Integer userId, Integer num);

    void deleteLikeDislikeReview(Integer idReview, Integer userId);

}
