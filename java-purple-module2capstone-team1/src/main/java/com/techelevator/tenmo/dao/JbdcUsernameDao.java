package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Username;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JbdcUsernameDao implements UsernameDao {
    private JdbcTemplate jdbcTemplate;

    public JbdcUsernameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //This will only select usernames and not other personal, sensitive information
    @Override
    public List<Username> getAllUsernames() {
        List<Username> users = new ArrayList<>();
        String sql = "SELECT username FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Username user = new Username();
            user.setUsername(results.getString("username"));
            users.add(user);
        }
        return users;
    }
}
