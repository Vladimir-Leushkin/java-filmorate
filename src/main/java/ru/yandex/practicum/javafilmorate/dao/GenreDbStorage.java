package ru.yandex.practicum.javafilmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }

    @Override
    public List<Genre> getGenreList() {
        String sql = "select * from GENRES";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs, rowNum));
        log.debug("Найдены жанры: {} ", genres);
        return genres;
    }

    @Override
    public Genre findGenreById(Integer id) {
        SqlRowSet GenreRows = jdbcTemplate.queryForRowSet("select * from GENRES where GENRE_ID = ?", id);
        Genre genre = null;
        if (GenreRows.next()) {
            genre = new Genre(
                    GenreRows.getInt("GENRE_ID"),
                    GenreRows.getString("GENRE_NAME")
            );
            log.debug("Найден жанр: {} {}", genre.getId(), genre.getName());
        } else {
            throw new NotFoundException("Жанр не найден");
        }
        return genre;
    }

    @Override
    public void setFilmGenre(Film film) {
        String sqlQueryDelete = "delete from FILM_GENRES where FILM_ID = ? ";
        jdbcTemplate.update(sqlQueryDelete, film.getId());
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        for (Genre genre : film.getGenres()) {
            String sqlQuery = "merge into FILM_GENRES(FILM_ID, GENRE_ID) " +
                    "values (?, ?) ";
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
        log.debug("Фильму {} установлены жанры: {} ", film.getId(), film.getGenres());
    }

    @Override
    public List<Genre> loadFilmGenre(Integer filmId) {
        String sql = "select * from GENRES G join FILM_GENRES FG on G.GENRE_ID = FG.GENRE_ID where FG.FILM_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs, rowNum), filmId);
        log.debug("У фильма {} найдены жанры: {} ", filmId, genres);
        return genres;
    }
}
