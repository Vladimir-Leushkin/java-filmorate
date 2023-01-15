package ru.yandex.practicum.javafilmorate.dao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final static int NEW_USEFUL = 0;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Review> getAllReview() {
        String sql = "SELECT r.ID_REVIEW AS id_review, r.CONTENT AS content" +
                ", r.is_positive AS is_positive, r.user_id AS user_id, r.film_id AS film_id" +
                ", SUM(rr.RATE) AS useful\n" +
                "FROM REVIEWS AS r\n" +
                "LEFT JOIN REVIEWS_RATINGS RR on r.ID_REVIEW = RR.ID_REVIEW\n" +
                "GROUP BY r.ID_REVIEW";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> getReview(rs, rowNum))).stream()
                .sorted((o1, o2) -> {
                    int result = Integer.valueOf(o1.getUseful()).compareTo(Integer.valueOf(o2.getUseful()));
                    return result * -1;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> getAllReviewByIdFilm(Integer filmId, Integer count) {
        String sql = "SELECT r.ID_REVIEW AS id_review, r.CONTENT AS content" +
                ", r.is_positive AS is_positive, r.user_id AS user_id, r.film_id AS film_id" +
                ", SUM(rr.RATE) AS useful\n" +
                "FROM REVIEWS AS r\n" +
                "LEFT JOIN REVIEWS_RATINGS RR on r.ID_REVIEW = RR.ID_REVIEW\n" +
                "WHERE r.FILM_ID=?\n" +
                "GROUP BY r.ID_REVIEW";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> getReview(rs, rowNum)), filmId).stream()
                .sorted((o1, o2) -> {
                    int result = Integer.valueOf(o1.getUseful()).compareTo(Integer.valueOf(o2.getUseful()));
                    return result * -1;
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Review findReviewById(Integer id) {
        String sql = "SELECT r.ID_REVIEW AS id_review, r.CONTENT AS content" +
                ", r.is_positive AS is_positive, r.user_id AS user_id, r.film_id AS film_id" +
                ", SUM(rr.RATE) AS useful\n" +
                "FROM REVIEWS AS r\n" +
                "LEFT JOIN REVIEWS_RATINGS RR on r.ID_REVIEW = RR.ID_REVIEW\n" +
                "WHERE r.ID_REVIEW=?\n" +
                "GROUP BY r.ID_REVIEW";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            return Review.builder()
                    .reviewId(rowSet.getInt("id_review"))
                    .content(rowSet.getString("content"))
                    .isPositive(rowSet.getBoolean("is_positive"))
                    .userId(rowSet.getInt("user_id"))
                    .filmId(rowSet.getInt("film_id"))
                    .useful(rowSet.getInt("useful"))
                    .build();
        }
        return null;
    }

    private static Review getReview(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .reviewId(rs.getInt("id_review"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .useful(rs.getInt("useful"))
                .build();
    }

    @Override
    public Review addReview(Review review) {
        String sqlQuery = "INSERT INTO reviews(CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID_REVIEW"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setInt(3, review.getUserId());
            stmt.setInt(4, review.getFilmId());
            stmt.setInt(5, 0);
            return stmt;
        }, keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return review;
    }

    @Override
    public Review changeReview(Review review) {
        String sql = "UPDATE reviews SET content = ?, is_positive = ? WHERE id_review = ?";
        jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getReviewId());
        return review;
    }

    @Override
    public void deleteReview(Integer id) {
        String sql = "DELETE FROM reviews WHERE id_review = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void addLikeDislikeReview(Integer idReview, Integer userId, Integer num) {
        String sqlQuery = "INSERT INTO reviews_ratings (id_review, user_id, rate) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, idReview, userId, num);
    }

    @Override
    public void changeLikeDislikeReview(Integer idReview, Integer userId, Integer num) {
        String sql = "UPDATE reviews_ratings SET rate = ? WHERE id_review = ? AND user_id = ?";
        jdbcTemplate.update(sql, num, idReview, userId);
    }

    @Override
    public void deleteLikeDislikeReview(Integer idReview, Integer userId) {
        String sql = "DELETE FROM reviews_ratings WHERE id_review=? AND user_id=?";
        jdbcTemplate.update(sql, idReview, userId);
    }
}
