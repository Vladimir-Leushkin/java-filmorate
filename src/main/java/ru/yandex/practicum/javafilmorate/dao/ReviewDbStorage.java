package ru.yandex.practicum.javafilmorate.dao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.review.ReviewStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final static int NEW_USEFUL = 0;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate, FilmDbStorage filmDbStorage, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public List<Review> getAllReview() {
        String sql = "SELECT * FROM reviews";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> getReview(rs, rowNum))).stream()
                .sorted((o1, o2) -> {
                    int result = Integer.valueOf(o1.getUseful()).compareTo(Integer.valueOf(o2.getUseful()));
                    return result * -1;})
                .collect(Collectors.toList());
    }

    public static Review getReview(ResultSet rs, int rowNum) throws SQLException {
        Review review =  new Review(rs.getString("content"),
                rs.getBoolean("is_positive"),
                rs.getInt("user_id"),
                rs.getInt("film_id"));
        review.setIdReview(rs.getInt("id_review"));
        review.setUseful(rs.getInt("useful"));
        return review;
    }

    @Override
    public Review addReview(Review review) {
        if (filmDbStorage.findFilmById(review.getFilmId()) instanceof Film &&
                userDbStorage.findUserById(review.getUserId()) instanceof User){
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            Map<String, Object> values = new HashMap<>();

            values.put("CONTENT", review.getContent());
            values.put("IS_POSITIVE", review.getIsPositive());
            values.put("USER_ID", review.getUserId());
            values.put("FILM_ID", review.getFilmId());
            values.put("USEFULNESS", 0);

            KeyHolder keyHolder = jdbcInsert
                    .withTableName("REVIEWS")
                    .usingGeneratedKeyColumns("REVIEW_ID")
                    .executeAndReturnKeyHolder(values);
            review.setIdReview(keyHolder.getKey().intValue());

            return review;
        } else {
            throw new ValidationException("Некорректные данные, отзыв не добавлен");
        }
    }


    @Override
    public void deleteReview(Integer id) {
        String sql = "DELETE FROM reviews WHERE id_review = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Review changeReview(Review review) {
        if (filmDbStorage.findFilmById(review.getFilmId()) instanceof Film &&
                userDbStorage.findUserById(review.getUserId()) instanceof User){
            String sql = "UPDATE reviews SET content = ?, is_positive = ? WHERE id_review = ?";
            jdbcTemplate.update(sql,
                    review.getContent(),
                    review.getIsPositive(),
                    review.getIdReview()
            );
            return review;
        } else {
            throw new ValidationException("Проверьте отзыв на корректность.");
        }
    }

    @Override
    public Review findReviewById(Integer id) {
        String sql = "SELECT * FROM reviews WHERE id_review = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()){
            Review review = new Review(
                    rowSet.getString("content"),
                    rowSet.getBoolean("is_positive"),
                    rowSet.getInt("user_id"),
                    rowSet.getInt("film_id")
            );
            review.setIdReview(rowSet.getInt("id_review"));
            review.setUseful(rowSet.getInt("useful"));
            return review;
        } else {
            throw new NotFoundException("Review с id " + id + " не найден.");
        }
    }
}
