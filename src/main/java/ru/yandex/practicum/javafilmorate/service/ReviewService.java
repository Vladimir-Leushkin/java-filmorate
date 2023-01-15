package ru.yandex.practicum.javafilmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.enums.EventType;
import ru.yandex.practicum.javafilmorate.enums.OperationType;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.dao.ReviewStorage;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final EventService eventService;
    private final static Integer LIKE = 1;

    private final static Integer DISLIKE = -1;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage, EventService eventService) {
        this.reviewStorage = reviewStorage;
        this.eventService = eventService;
    }

    public List<Review> voidReviews(Integer filmId, Integer count) {
        if (filmId == -1) {
            return getAllReview();
        }
        return getAllReviewByIdFilm(filmId, count);
    }

    public List<Review> getAllReview() {
        return reviewStorage.getAllReview();
    }

    public List<Review> getAllReviewByIdFilm(Integer filmId, Integer count) {
        return reviewStorage.getAllReviewByIdFilm(filmId, count);
    }

    public Review findReviewById(Integer id) {
        Review review = reviewStorage.findReviewById(id);
        if (review == null) {
            throw new NotFoundException("Ревью не найдено");
        }
        return reviewStorage.findReviewById(id);
    }

    public Review addReview(Review review) {
        if (review.getUserId() < 0 || review.getUserId() == null
                || review.getFilmId() < 0 || review.getFilmId() == null
                || review.getContent().isEmpty() || review.getIsPositive() == null) {
            throw new NotFoundException("Поля в отзыве не могут быть пустыми или отрицательными");
        }
        Review addReview = reviewStorage.addReview(review);
        eventService.addEvent(review.getUserId(), review.getReviewId(), EventType.REVIEW, OperationType.ADD);
        return addReview;
    }

    public Review changeReview(Review review) {
        eventService.addEvent(
                findReviewById(review.getReviewId()).getUserId()
                , review.getReviewId()
                , EventType.REVIEW
                , OperationType.UPDATE);
        return reviewStorage.changeReview(review);
    }

    public void deleteReview(Integer id) {
        Review review = findReviewById(id);
        eventService.addEvent(review.getUserId(), review.getReviewId(), EventType.REVIEW, OperationType.REMOVE);
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
