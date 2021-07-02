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

    @Override
    public List<Transfer> list(Account account) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE account_from = ? OR account_to = ?;";
        int userAccount = account.getAccountId();
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userAccount, userAccount);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfer.setToUserName(getAccountByUserId(transfer.getToUserId()).getToUserName());
            transfer.setFromUserName(getAccountByUserId(transfer.getFromUserId()).getFromUserName());
            transfers.add(transfer);
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

        return transfer;
    }
}