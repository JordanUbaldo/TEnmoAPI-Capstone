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
    public Account getAccount(int accountId, int userId) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE account_id = ? AND user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, userId);
        if (results.next()) {
           account = mapRowToAccount(results);
        } else {
            throw new AccountNotFoundException();
        }
        return account;
    }

    @Override
    public void transfer(BigDecimal amountToTransfer, int from, int to, int fromUserId, int toUserId) {
        subtractToBalance(amountToTransfer, from, fromUserId);
        addToBalance(amountToTransfer, to, toUserId);
    }

    @Override
    public void addToBalance(BigDecimal amountToAdd, int to, int userId) {
        BigDecimal updatedBalance = getAccount(to, userId).getBalance().add(amountToAdd);
        String sql = "UPDATE accounts SET balance = ? WHERE id = ? AND user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, updatedBalance, to, userId);

    }

    @Override
    public void subtractToBalance(BigDecimal amountToSubtract, int from, int userId) {
        BigDecimal updatedBalance = getAccount(from, userId).getBalance().add(amountToSubtract);
        String sql = "UPDATE accounts SET balance = ? WHERE id = ? AND user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, updatedBalance, from, userId);
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
