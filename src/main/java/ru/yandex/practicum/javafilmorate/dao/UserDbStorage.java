package ru.yandex.practicum.javafilmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("USER_NAME"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }

    @Override
    public List<User> getUsersList() {
        String sql = "select * from USERS";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs, rowNum));
        log.debug("Найдены пользователи: {} ", users);
        return users;
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into USERS(USER_NAME, EMAIL, LOGIN, BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        log.debug("Добавлен пользователь с идентификатором {} ", user.getId());
        return user;
    }

    @Override
    public User findUserById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("USER_ID"),
                    userRows.getString("USER_NAME"),
                    userRows.getString("EMAIL"),
                    userRows.getString("LOGIN"),
                    userRows.getDate("BIRTHDAY").toLocalDate()
            );
            log.debug("Найден пользователь: {} {}", user.getId(), user.getName());
            return user;
        } else {
            log.debug("Пользователь с идентификатором {} не найден.", id);
            return null;
        }
    }

    @Override
    public void update(User user) {
        String sqlQuery = "update USERS set " +
                "USER_NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ?  where USER_ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getEmail()
                , user.getLogin()
                , user.getBirthday()
                , user.getId());
        log.debug("Обновлена информация о пользователе: {}", user);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        String sqlQuery = "insert into USER_FRIENDS(USER_ID, FRIEND_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
        log.debug("Пользователю : {}, добавлен друг {}", id, friendId);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        String sqlQuery = "delete from USER_FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
        log.debug("У пользователя: {}, удален друг {}", id, friendId);
    }

    @Override
    public List<Integer> findAllFriendsId(Integer id) {
        String sql = "select FRIEND_ID from USER_FRIENDS where USER_ID = ?";
        List<Integer> friendsId = jdbcTemplate.queryForList(sql, Integer.class, id);
        log.debug("У пользователя: {}, найдены друзья {}", id, friendsId);
        return friendsId;
    }

    @Override
    public List<User> findAllFriends(Integer id) {
        String sql = "select U.* from USERS U " +
                "join USER_FRIENDS UF1 on U.user_id = UF1.friend_id and UF1.user_id = ?";
        List<User> friends = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs, rowNum), id);
        log.debug("У пользователя: {} найдены друзья {}", id, friends);
        return friends;
    }

    @Override
    public List<Integer> findCommonFriendsId(Integer id, Integer otherId) {
        String sql = "select U.friend_id from USER_FRIENDS U " +
                "join USER_FRIENDS UF on U.user_id = UF.friend_id and UF.user_id = ?" +
                "and U.user_id = ?";
        List<Integer> commonFriendsId = jdbcTemplate.queryForList(sql, Integer.class, id, otherId);
        log.debug("У пользователя: {} и пользователя: {} найдены общие друзья {}", id, otherId, commonFriendsId);
        return commonFriendsId;

    }

    @Override
    public List<User> findCommonFriends(Integer id, Integer otherId) {
        String sql = "select u.* from USERS u " +
                "join USER_FRIENDS UF1 on u.user_id = UF1.friend_id and UF1.user_id = ?" +
                "join USER_FRIENDS UF2 on u.user_id = UF2.friend_id and UF2.user_id = ?";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs, rowNum), id, otherId);
        log.debug("У пользователя: {} и пользователя: {} найдены общие друзья {}", id, otherId, users);
        return users;
    }

    @Override
    public void deleteUser(Integer id) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
        log.debug("Удален пользователь: {}", id);
    }
}
