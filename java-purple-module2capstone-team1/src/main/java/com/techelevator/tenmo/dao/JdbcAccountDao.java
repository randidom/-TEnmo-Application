package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public BigDecimal getAccountBalance(String username) {
        BigDecimal balance = null;
        String sql = "SELECT balance, account.user_id, account_id FROM account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE username ILIKE ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
                if (rowSet.next()) {
                    balance = mapRowToAccount(rowSet).getBalance();

                }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to database or server");

        }
        return balance;
    }

    private Account mapRowToAccount(SqlRowSet results){
        Account account = new Account();
        account.setAccountId(results.getInt("account_id"));
        account.setBalance(results.getBigDecimal("balance"));
        account.setUserId(results.getInt("user_id"));

        return account;
    }


}




