package ru.yandex.practicum.javafilmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Director;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.model.Mpa;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Repository
@Slf4j
@Component
public class DirectorDbStorage implements DirectorStorage {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private Director makeDirector(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getInt("DIRECTOR_ID"),
                rs.getString("DIRECTOR_NAME")
        );
    }

    @Override
    public Director addDirector(Director director) {
        String sqlQuery = "insert into DIRECTORS(DIRECTOR_NAME) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"DIRECTOR_ID"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        director.setId(keyHolder.getKey().intValue());
        log.debug("Добавлен режиссер с идентификатором {} ", director.getId());
        return director;
    }

    @Override
    public Integer deleteDirectorById(Integer id) {
        return jdbcTemplate.update("DELETE from DIRECTORS where DIRECTOR_ID = ?",id);
    }

    @Override
    public Integer updateDirector(Director director) {
        String sqlQuery = "UPDATE DIRECTORS SET DIRECTOR_NAME=? WHERE DIRECTOR_ID=?;";

        return jdbcTemplate.update(connection -> {
               PreparedStatement stmt = connection.prepareStatement(sqlQuery);
               stmt.setString(1, director.getName());
               stmt.setInt(2, director.getId());
               return stmt;
        });
    }

    @Override
    public List<Director> findDirectorsByFilmId(Integer filmId) {
        String sql = "select * from DIRECTORS D " +
                      "join FILM_DIRECTORS FD on D.DIRECTOR_ID = FD.DIRECTOR_ID where FD.FILM_ID = ?";
        List<Director> directors = jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs, rowNum), filmId);
        return directors;
    }

    @Override
    public void addFilmDirectors(Film film) {
        String sqlQueryDelete = "delete from FILM_DIRECTORS where FILM_ID = ? ";
        jdbcTemplate.update(sqlQueryDelete, film.getId());
        if (film.getDirectors() == null || film.getDirectors().isEmpty()) {
            return;
        }
        for (Director director : film.getDirectors()) {
            String sqlQuery = "merge into FILM_DIRECTORS(FILM_ID, DIRECTOR_ID) " +
                    "values (?, ?) ";
            jdbcTemplate.update(sqlQuery, film.getId(), director.getId());
        }
        log.debug("Film {} directors {} added ", film.getId(), film.getGenres());
    }

    @Override
    public List<Director> getDirectorList() {
        String sql = "select * from DIRECTORS";
        List<Director> allDirectors = jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs, rowNum));
        log.debug("Найдены режиссеры: {} ", allDirectors);
        return allDirectors;
    }

    @Override
    public Director findDirectorById(Integer id) {
        return jdbcTemplate.queryForObject("select * from DIRECTORS " +
                        "where DIRECTOR_ID = ?",
                (rs, rowNum) -> makeDirector(rs, rowNum),id);
    }


}
