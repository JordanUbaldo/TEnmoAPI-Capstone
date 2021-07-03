package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
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
        Transfer transfer = new Transfer();
//       try {
            String sql = "SELECT t.*, u.username AS from_username, v.username AS to_username, ts.transfer_status_desc, tt.transfer_type_desc " +
                    "FROM transfers t " +
                    "JOIN accounts a ON t.account_from = a.account_id " +
                    "JOIN accounts b ON t.account_to = b.account_id " +
                    "JOIN users u ON a.user_id = u.user_id " +
                    "JOIN users v ON b.user_id = v.user_id " +
                    "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " +
                    "JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " +
                    "WHERE t.transfer_id = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
//       } catch (DataAccessException e) {
//            System.out.println("Error: Not a valid transfer.");
//        }
        return transfer;
    }

    @Override
    public List<Transfer> list(Account account) {
        List<Transfer> transfers = new ArrayList<>();
        try {
            Transfer transfer = new Transfer();
            String sql = "SELECT t.*, u.username AS from_username, v.username AS to_username, ts.transfer_status_desc, tt.transfer_type_desc " +
                    "FROM transfers t " +
                    "JOIN accounts a ON t.account_from = a.account_id " +
                    "JOIN accounts b ON t.account_to = b.account_id " +
                    "JOIN users u ON a.user_id = u.user_id " +
                    "JOIN users v ON b.user_id = v.user_id " +
                    "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " +
                    "JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " +
                    "WHERE a.user_id = ? OR b.user_id = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account.getUserId(), account.getUserId());
            while (results.next()) {
                transfer = mapRowToTransfer(results);
                transfers.add(transfer);

            }
        } catch (DataAccessException e) {
            System.out.println("Unable to access data.");
        }
        return transfers;
    }

    // How to avoid transfer being put into the database, but not being performed?
    @Override
    public void transfer(Transfer transfer) {
        //try {
            setTransfer(transfer);
            BigDecimal amountToTransfer = transfer.getAmount();
            int fromUser = transfer.getFromUserId();
            int toUser = transfer.getToUserId();
//        if (accountDao.getAccount(fromUser).getBalance().compareTo(amountToTransfer) > -1) {
            addToBalance(amountToTransfer, toUser);
            subtractToBalance(amountToTransfer, fromUser);
        //} catch (Exception e) {
//            System.out.println("Unable to perform transfer.");
//        }
    }

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
        try {
            String sql = "SELECT * FROM accounts WHERE user_id = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (DataAccessException e) {
            System.out.println("Unable to access data.");
        }
        return account;
    }

    public Account getAccountByAccountId(int accountId) {
        Account account = null;
        try {
        String sql = "SELECT * FROM accounts WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        } catch (DataAccessException e) {
        System.out.println("Unable to access data.");
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
        transfer.setToUserName(rs.getString("to_username"));
        transfer.setStatusDesc(rs.getString("transfer_status_desc"));
        transfer.setTypeDesc(rs.getString("transfer_type_desc"));

        return transfer;
    }
}