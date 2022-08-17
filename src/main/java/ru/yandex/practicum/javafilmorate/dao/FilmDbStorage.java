package ru.yandex.practicum.javafilmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                mpaStorage.findMpaById(rs.getInt("MPA_ID")),
                genreStorage.loadFilmGenre(rs.getInt("FILM_ID"))
        );
    }

    @Override
    public List<Film> getFilmsList() {
        String sql = "select F.*, " +
                "M.MPA_NAME " +
                "from FILMS F " +
                "LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID ";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs, rowNum));
        log.debug("Найдены фильмы: {} ", films);
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into FILMS(FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        genreStorage.setFilmGenre(film);
        log.debug("Добавлен фильм с идентификатором {} ", film.getId());
        return findFilmById(film.getId());
    }

    @Override
    public Film findFilmById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select F.*, " +
                " M.MPA_NAME " +
                " from FILMS F " +
                " LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID where FILM_ID = ?", id);
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("FILM_ID"),
                    filmRows.getString("FILM_NAME"),
                    filmRows.getString("DESCRIPTION"),
                    filmRows.getDate("RELEASE_DATE").toLocalDate(),
                    filmRows.getInt("DURATION"),
                    mpaStorage.findMpaById(filmRows.getInt("MPA_ID")),
                    genreStorage.loadFilmGenre(filmRows.getInt("FILM_ID"))
            );
            log.debug("Найден фильм: {} {}", film.getId(), film.getName());
            return film;
        } else {
            log.debug("Фильм с идентификатором {} не найден.", id);
            return null;
        }
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "update FILMS set " +
                "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());
        genreStorage.setFilmGenre(film);
        log.debug("Обновлена информация о фильме: {}", film);
    }

    @Override
    public List<Film> getPopularFilmsList(Integer count) {
        String sql = "select f.*, COUNT(fl.FILM_ID) as c from FILMS f " +
                "left join FILM_LIKES FL on f.film_id = FL.film_id " +
                "group by f.film_id, film_name, description, release_date, duration, mpa_id " +
                "order by c desc " +
                "limit ?";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs, rowNum), count);
        log.debug("Найдены фильмы: {} ", films);
        return films;
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        String sqlQuery = "insert into FILM_LIKES(FILM_ID, USER_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
        log.debug("Фильму: {}, добавлен лайк от пользователя: {}", id, userId);
    }

    @Override
    public void deleteLike(Integer id, Integer userId) {
        String sqlQuery = "delete from  FILM_LIKES where FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
        log.debug("Фильму: {}, удален лайк от пользователя: {}", id, userId);
    }

    @Override
    public void deleteFilm(Integer id) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
        log.debug("Удален фильм: {}", id);
    }

}
