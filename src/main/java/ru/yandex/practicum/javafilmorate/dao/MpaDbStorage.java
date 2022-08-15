package ru.yandex.practicum.javafilmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME")
        );
    }

    @Override
    public List<Mpa> getMpaList() {
        String sql = "select * from MPA";
        List<Mpa> mpas = jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs, rowNum));
        log.debug("Найдены рейтинги: {} ", mpas);
        return mpas;
    }

    @Override
    public Mpa findMpaById(Integer id) {
        SqlRowSet MpaRows = jdbcTemplate.queryForRowSet("select * from MPA where MPA_ID = ?", id);
        Mpa mpa = null;
        if (MpaRows.next()) {
            mpa = new Mpa(
                    MpaRows.getInt("MPA_ID"),
                    MpaRows.getString("MPA_NAME")
            );
            log.debug("Найден рейтинг: {} {}", mpa.getId(), mpa.getName());
        } else {
            throw new NotFoundException("Рейтинг не найден");
        }
        return mpa;
    }
}
