package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

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
    public List<Transfer> list() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfers;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public void transfer(BigDecimal amountToTransfer, int fromUser, int toUser) {
        accountDao.subtractToBalance(amountToTransfer, fromUser);
        accountDao.addToBalance(amountToTransfer, toUser);
    }

    @Override
    public Transfer getTransfer(BigDecimal amountToTransfer, int fromUser, int toUser){
        int fromAccount = accountDao.getAccount(fromUser).getAccountId();
        int toAccount = accountDao.getAccount(toUser).getAccountId();

        Transfer transfer = new Transfer();
        transfer.setAmount(amountToTransfer);
        transfer.setFrom(fromAccount);
        transfer.setTo(toAccount);
        return transfer;
    }

    @Override
    public void setTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfers (account_from, account_to, amount)" +
                     "VALUES (? ,?, ?);";
        int from = transfer.getFrom();
        int to = transfer.getTo();
        BigDecimal amount = transfer.getAmount();
        jdbcTemplate.queryForRowSet(sql, from, to, amount);
    }

    @Override
    public void setTransferType(Transfer transfer, String description) {
        String sql = "INSERT INTO transfer_types (transfer_type_id, transfer_type_desc)" +
                     "VALUES (? ,?);";
        int  id = transfer.getTypeId();
        jdbcTemplate.queryForRowSet(sql, id, description);
    }

    @Override
    public void setTransferStatus(Transfer transfer, String description) {
        String sql = "INSERT INTO transfer_status_id (transfer_type_id, transfer_status_desc);" +
                      "VALUES (? ,?);";
        int  id = transfer.getStatusId();
        jdbcTemplate.queryForRowSet(sql, id, description);


    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTypeId(rs.getInt("transfer_type_id"));
        transfer.setStatusId(rs.getInt("transfer_status_id"));
        transfer.setFrom(rs.getInt("account_from"));
        transfer.setFrom(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));

        return transfer;
    }
}