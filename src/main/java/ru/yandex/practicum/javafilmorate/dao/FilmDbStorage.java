package ru.yandex.practicum.javafilmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final DirectorStorage directorStorage;

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                mpaStorage.findMpaById(rs.getInt("MPA_ID")),
                genreStorage.loadFilmGenre(rs.getInt("FILM_ID")),
                directorStorage.findDirectorsByFilmId(rs.getInt("FILM_ID"))
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
        directorStorage.addFilmDirectors(film);
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
                    genreStorage.loadFilmGenre(filmRows.getInt("FILM_ID")),
                    directorStorage.findDirectorsByFilmId(filmRows.getInt("FILM_ID"))
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
        directorStorage.addFilmDirectors(film);
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
    public List<Film> findCommonByUser(Integer userId, Integer friendId) {
        String sql = "select f.*, COUNT(fl.FILM_ID) as c from FILMS f " +
                "left join FILM_LIKES FL on f.film_id = FL.film_id " +
                "where f.film_id in (select L1.film_id " +
                "from FILM_LIKES as L1 " +
                "inner join FILM_LIKES as L2 on L1.film_id = L2.film_id " +
                "where L1.user_id = ? and L2.user_id = ? " +
                "group by L1.film_id) " +
                "group by f.film_id, film_name, description, release_date, duration, mpa_id " +
                "order by c desc";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs, rowNum), userId, friendId);
        log.debug("Найдены общие фильмы: {} ", films);
        return films;
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        String sqlQuery = "MERGE into FILM_LIKES(FILM_ID, USER_ID) " +
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

    public List<Film> getFilmsListDirector(Integer directorId, String sortBy) {
        String sql = "";
        if (sortBy.equals("year")) {
            sql = "select f.*, COUNT(fl.FILM_ID) as likes from FILMS f " +
                    "left join FILM_LIKES FL on f.film_id = FL.film_id " +
                    "LEFT JOIN FILM_DIRECTORS FD on F.FILM_ID = FD.FILM_ID " +
                    "WHERE FD.DIRECTOR_ID = ? " +
                    "group by f.film_id, film_name, description, release_date, duration, mpa_id " +
                    "order by F.RELEASE_DATE ASC ";
        } else if (sortBy.equals("likes")) {
            sql = "select f.*, COUNT(fl.FILM_ID) as likes from FILMS f " +
                    "left join FILM_LIKES FL on f.film_id = FL.film_id " +
                    "LEFT JOIN FILM_DIRECTORS FD on F.FILM_ID = FD.FILM_ID " +
                    "WHERE FD.DIRECTOR_ID = ? " +
                    "group by f.film_id, film_name, description, release_date, duration, mpa_id " +
                    "order by likes desc ";
        }

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs, rowNum), directorId);
        log.debug("Найдены фильмы: {} ", films);
        return films;

    }

    @Override
    public List<Film> searchFilms(String query, String byConditions) {
        boolean byTitle = byConditions.toLowerCase().contains("title");
        boolean byDirector = byConditions.toLowerCase().contains("director");
        String queryExpression = "%" + query.toLowerCase() + "%";
        Object[] queryObj = new Object[]{queryExpression};
        if (byTitle || byDirector) {
            String sql = "select f.film_id as film_id, f.film_name as film_name, f.description as description, " +
                    "f.release_date as release_date, f.duration as duration, f.mpa_id as mpa_id " +
                    "from FILMS as f " +
                    "left join FILM_LIKES as fl on fl.FILM_ID = f.film_id " +
                    "where ";
            if (byTitle) {
                sql += "lower (f.film_name) LIKE ? ";
                if (byDirector) {
                    sql += "or ";
                    queryObj = new Object[]{queryExpression, queryExpression};
                }
            }
            if (byDirector) {
                sql += "f.film_id in (select f1.film_id " +
                        "from FILMS as f1 " +
                        "inner join FILM_DIRECTORS fd on fd.FILM_ID = f1.FILM_ID " +
                        "inner join DIRECTORS d on d.DIRECTOR_ID = fd.DIRECTOR_ID " +
                        "where lower(d.DIRECTOR_NAME) LIKE ?) ";
            }
            sql += "GROUP BY f.film_id ORDER BY COUNT(fl.FILM_ID) DESC";
            List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs, rowNum), queryObj);
            log.debug("По запросу найдены фильмы: {} ", films);
            return films;
        } else return null;
    }

    public List<Integer> findFilmIdUserLikes(Integer userId) {
        String sqlLikes = "select film_id from FILM_LIKES " +
                " WHERE user_id = ?";
        List<Integer> userLikes = jdbcTemplate.queryForList(sqlLikes, Integer.class, userId);
        return userLikes;
    }

    @Override
    public List<Film> recommendations(Integer id) {
        List<Film> recommendations = new ArrayList<>();
        String sqlUser = "select FL.user_id " +
                "from FILM_LIKES FL " +
                "where FL.film_id in (select film_id from FILM_LIKES WHERE user_id = ?) " +
                "and user_id <> ?" +
                "group by FL.user_id " +
                "having count(FL.film_id) > 0 " +
                "order by count(FL.film_id) desc " +
                "limit 1;";
        List<Integer> userId = jdbcTemplate.queryForList(sqlUser, Integer.class, id, id);
        if (userId.size() == 1) {
            String sqlFilms = "select F.* " +
                    "from FILM_LIKES FL " +
                    "left join FILMS F on FL.film_id = F.film_id " +
                    "where FL.film_id not in (select film_id from FILM_LIKES  where user_id = ?) " +
                    "and FL.user_id = ?;";
            recommendations = jdbcTemplate.query(sqlFilms, (rs, rowNum) -> makeFilm(rs, rowNum), id, userId.get(0));
            log.debug("Рекомендованы фильмы: {} ", recommendations);
        }
        return recommendations;
    }
}
