package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccount(int userId) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
           account = mapRowToAccount(results);
        } else {
            throw new AccountNotFoundException();
        }
        return account;
    }

    @Override
    public void addToBalance(BigDecimal amountToAdd, int to) {
        BigDecimal updatedBalance = getAccount(to).getBalance().add(amountToAdd);
        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?;";
        jdbcTemplate.queryForRowSet(sql, updatedBalance, to);

    }

    @Override
    public void subtractToBalance(BigDecimal amountToSubtract, int from) {
        BigDecimal updatedBalance = getAccount(from).getBalance().subtract(amountToSubtract);
        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?;";
        jdbcTemplate.queryForRowSet(sql, updatedBalance, from);
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
