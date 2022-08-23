package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.dao.UserDbStorage;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.review.ReviewStorage;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final static Integer LIKE = 1;

    private final static Integer DISLIKE = -1;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    public List<Review> getAllReview() {
        return reviewStorage.getAllReview();
    }

    public List<Review> getAllReviewByIdFilm(Integer filmId, Integer count) {
        return reviewStorage.getAllReviewByIdFilm(filmId, count);
    }

    public Review findReviewById(Integer id) {
        return reviewStorage.findReviewById(id);
    }


    public Review addReview(Review review) {
        if (review.getUserId() < 0 || review.getUserId() == null
                || review.getFilmId() < 0 || review.getFilmId() == null
                || review.getContent().isEmpty() || review.getIsPositive() == null) {
            throw new NotFoundException("test");
        }
        return reviewStorage.addReview(review);
    }

    public Review changeReview(Review review) {
        return reviewStorage.changeReview(review);
    }

    public void deleteReview(Integer id) {
        reviewStorage.deleteReview(id);
    }

    public void addLikeReview(Integer id, Integer userId) {
        reviewStorage.addLikeDislikeReview(id, userId, LIKE);
    }

    public void addDislikeReview(Integer id, Integer userId) {
        reviewStorage.addLikeDislikeReview(id, userId, DISLIKE);
    }

    public void deleteLikeReview(Integer id, Integer userId) {
        reviewStorage.deleteLikeDislikeReview(id, userId);
    }
}
