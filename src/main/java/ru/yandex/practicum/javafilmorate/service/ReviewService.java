package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.dao.UserDbStorage;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.review.ReviewStorage;

import java.util.List;

@Component
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    private final static Integer LIKE = 1;

    private final static Integer DISLIKE = -1;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage, JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.reviewStorage = reviewStorage;
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    public Review addReview(Review review) {
        return reviewStorage.addReview(review);
    }

    public Review changeReview(Review review) {
        return reviewStorage.changeReview(review);
    }

    public void deleteReview(Integer id) {
        reviewStorage.deleteReview(id);
    }

    public Review findReviewById(Integer id) {
        return reviewStorage.findReviewById(id);
    }

    public List<Review> getAllReview() {
        return reviewStorage.getAllReview();
    }
    public List<Review> getAllReviewByIdFilm(Integer filmId, Integer count){
        return reviewStorage.getAllReviewByIdFilm(filmId, count);
    }

    public void changeUseful(Integer id, Integer num){
        reviewStorage.changeUseful(id, num);
    }

    public void addLikeReview(Integer id, Integer userId){
        addLikeDislike(id, userId, LIKE);
    }

    public void addDislikeReview(Integer id, Integer userId){
        addLikeDislike(id, userId, DISLIKE);
    }

    public void deleteLikeReview(Integer id, Integer userId){
        deleteLikeDislike(id, userId, DISLIKE);
    }

    public void deleteDislikeReview(Integer id, Integer userId) {
        deleteLikeDislike(id, userId, LIKE);
    }

    public void addLikeDislike(Integer id, Integer userId, Integer value){
        if (reviewStorage.findReviewById(id) instanceof Review &&
                userDbStorage.findUserById(userId) instanceof User){
            String sqlQuery = "SELECT * FROM reviews_ratings WHERE user_id = ? AND id_review = ?";
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId, id);
            if (rowSet.next()){
                String sqlUpdate = "UPDATE reviews_ratings SET rate = ? WHERE user_id = ? AND id_review = ?";
                jdbcTemplate.update(sqlUpdate, value, userId, id);
            } else {
                String sql = "INSERT INTO reviews_ratings (id_review, user_id, rate) VALUES (?,?,?)";
                jdbcTemplate.update(sql, id, userId, value);
            }
            changeUseful(id, value);
        } else {
            throw new NotFoundException("Отсутствует пользователь или отзыв");
        }
    }

    public void deleteLikeDislike(Integer id, Integer userId, Integer value){
        if (reviewStorage.findReviewById(id) instanceof Review &&
                userDbStorage.findUserById(userId) instanceof User){
            changeUseful(id, value);
            String sql = "DELETE FROM reviews_ratings WHERE user_id = ? AND id_review = ?";
            jdbcTemplate.update(sql, userId, id);
        } else {
            throw new NotFoundException("Отсутствует пользователь или отзыв");
        }
    }
}
