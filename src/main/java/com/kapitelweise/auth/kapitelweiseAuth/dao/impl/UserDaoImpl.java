package com.kapitelweise.auth.kapitelweiseAuth.dao.impl;

import com.kapitelweise.auth.kapitelweiseAuth.dao.UserDao;
import com.kapitelweise.auth.kapitelweiseAuth.domain.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

//die konkrete Implementierung, hier führen wir unten die Datenbankabfrage durch.
//@Repository ist ein Spring Annotation, die sagt, dass diese Klasse ein Repository ist, also eine Datenzugriffsschicht.
// d.h. UserDao --> UserDaoImpl und --> AppUser arbeiten quasi zusammen.


@Repository
public class UserDaoImpl implements UserDao {


    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean userExists(String username) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE username = ?", Integer.class, username);
        return count > 0;
    }

    @Override
    public AppUser saveUser(AppUser user) {
        return jdbcTemplate.queryForObject(
                "INSERT INTO users (username, password) VALUES (?, ?) RETURNING id, username, password, created_at",
                (rs, rowNum) -> new AppUser(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at").toLocalDateTime()),
                user.getUsername(), user.getPassword());
    }



    @Override
   public Optional<AppUser> findByUsername(String username){
    List <AppUser> users = jdbcTemplate.query("Select username, password from users where username = ? Limit 1",
    new UserRowMapper(), username);

    return users.stream().findFirst();
   }


   public static class UserRowMapper implements RowMapper<AppUser> {
        @Override
        public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AppUser(rs.getString("username"), rs.getString("password"));
        }
    }
}