package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int id) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        Account account = mapRowToAccount(results);
        return account.getBalance();
    }

    @Override
    public void transfer(BigDecimal amountToTransfer, int from, int to) {
        String sqlFrom ="SELECT balance FROM accounts WHERE account_id = ?";
        SqlRowSet resultsFrom = jdbcTemplate.queryForRowSet(sqlFrom, from);
        BigDecimal fromBalance = resultsFrom.getBigDecimal("balance");

        if (fromBalance.compareTo(amountToTransfer) >= 0) {
            BigDecimal fromBalanceAfterTransfer = fromBalance.subtract(amountToTransfer);
            String sqlFromUpdate = "UPDATE accounts SET balance = ? WHERE id = ?;";
            SqlRowSet resultsFromUpdate = jdbcTemplate.queryForRowSet(sqlFromUpdate, fromBalanceAfterTransfer, from);

            String sqlTo = "SELECT * FROM accounts WHERE account_id = ?";
            SqlRowSet resultsTo = jdbcTemplate.queryForRowSet(sqlTo, to);
            BigDecimal toBalance = resultsFrom.getBigDecimal("balance");
            BigDecimal toBalanceAfterTransfer = toBalance.add(amountToTransfer);
            String sqlToUpdate = "UPDATE accounts SET balance = ? WHERE id = ?;";
            SqlRowSet resultsToUpdate = jdbcTemplate.queryForRowSet(sqlToUpdate, toBalanceAfterTransfer, to);
        } else {
            System.out.println("Transfer cannot be performed.");
        }
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
