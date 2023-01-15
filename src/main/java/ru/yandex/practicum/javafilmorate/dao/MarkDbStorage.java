package ru.yandex.practicum.javafilmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Mark;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class MarkDbStorage implements MarkStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MarkDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Mark makeMarkByFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Mark(rs.getInt("FILM_ID"),
                rs.getInt("USER_ID"),
                rs.getInt("MARK")
        );
    }

    @Override
    public List<Mark> getMarkList() {
        String sql = "select * from GENRES";
        List<Mark> marks = jdbcTemplate.query(sql, (rs, rowNum) -> makeMarkByFilm(rs, rowNum));
        log.debug("Найдены оценки: {} ", marks);
        return marks;
    }


    @Override
    public void setFilmMarks(Film film) {
        String sqlQueryDelete = "delete from FILM_LIKES where FILM_ID = ? ";
        jdbcTemplate.update(sqlQueryDelete, film.getId());
        if (film.getMarks() == null || film.getMarks().isEmpty()) {
            return;
        }
        for (Mark mark : film.getMarks()) {
            String sqlQuery = "merge into FILM_LIKES(FILM_ID, USER_ID, MARK) " +
                    "values (?, ?, ?) ";
            jdbcTemplate.update(sqlQuery, film.getId(), mark.getUserId(), mark.getMark());
        }
        log.debug("Фильму {} установлены оценки: {} ", film.getId(), film.getMarks());
    }

    @Override
    public List<Mark> loadFilmMark(Integer filmId) {
        String sql = "select * from FILM_LIKES where FILM_ID = ?";
        List<Mark> marks = jdbcTemplate.query(sql, (rs, rowNum) -> makeMarkByFilm(rs, rowNum), filmId);
        log.debug("У фильма {} найдены жанры: {} ", filmId, marks);
        return marks;
    }

}
