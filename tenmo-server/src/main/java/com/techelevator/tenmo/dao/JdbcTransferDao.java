package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransfer(int id) {
        String sql = "SELECT * FROM transfers WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        Transfer transfer = mapRowToTransfer(results);
        return transfer;
    }
// Doesn't work
    @Override
    public List<Transfer> list(Account account) {
        List<Transfer> transfers = new ArrayList<>();
        Transfer transfer = new Transfer();
        String sql = "SELECT *, username AS from_username " +
                "FROM transfers t " +
                "JOIN accounts a1 ON t.account_from = a1.account_id " +
                "JOIN users u USING(user_id) " +
                "WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account.getAccountId(), account.getAccountId());
        while (results.next()) {
            transfer = mapRowToTransfer(results);
            transfers.add(transfer);

        }
        String sql2 = "SELECT username AS to_username " +
                "FROM transfers t " +
                "JOIN accounts a1 ON t.account_to = a1.account_id " +
                "JOIN users u USING(user_id) " +
                "WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results2 = jdbcTemplate.queryForRowSet(sql2, account.getAccountId(), account.getAccountId());
        while (results2.next()) {
            for (int i = 0; i < transfers.size(); i++) {
                transfers.get(i).setToUserName(results2.getString("to_username"));
            }
        }
        return transfers;
    }

    @Override
    public void transfer(Transfer transfer) {
        BigDecimal amountToTransfer = transfer.getAmount();
        int fromUser = transfer.getFromUserId();
        int toUser = transfer.getToUserId();
//        if (accountDao.getAccount(fromUser).getBalance().compareTo(amountToTransfer) > -1) {
        addToBalance(amountToTransfer, toUser);
        subtractToBalance(amountToTransfer, fromUser);
    }

    @Override
    public void setTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfers (account_from, account_to, amount, transfer_type_id, transfer_status_id)" +
                "VALUES (? ,?, ?, ?, ?);";
        int from = getAccountByUserId(transfer.getFromUserId()).getAccountId();
        int to = getAccountByUserId(transfer.getToUserId()).getAccountId();
        int typeId = transfer.getTypeId();
        int statusId = transfer.getStatusId();
        BigDecimal amount = transfer.getAmount();
        jdbcTemplate.update(sql, from, to, amount, typeId, statusId);
    }

    public void addToBalance(BigDecimal amountToAdd, int to) {
        BigDecimal updatedBalance = getAccountByUserId(to).getBalance().add(amountToAdd);
        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?;";
        jdbcTemplate.update(sql, updatedBalance, to);

    }

    public void subtractToBalance(BigDecimal amountToSubtract, int from) {
        BigDecimal updatedBalance = getAccountByUserId(from).getBalance().subtract(amountToSubtract);
        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?;";
        jdbcTemplate.update(sql, updatedBalance, from);
    }

    @Override
    public Account getAccountByUserId(int userId) {
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

    public Account getAccountByAccountId(int accountId) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            account = mapRowToAccount(results);
        } else {
            throw new AccountNotFoundException();
        }
        return account;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTypeId(rs.getInt("transfer_type_id"));
        transfer.setStatusId(rs.getInt("transfer_status_id"));
        transfer.setToUserId(getAccountByAccountId(rs.getInt("account_from")).getToUserId());
        transfer.setFromUserId(getAccountByAccountId(rs.getInt("account_to")).getFromUserId());
        transfer.setAmount(rs.getBigDecimal("amount"));
        transfer.setFromUserName(rs.getString("from_username"));

        return transfer;
    }
}