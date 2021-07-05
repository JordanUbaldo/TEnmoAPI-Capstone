package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import java.util.List;

public interface TransferDao {

    Transfer getTransfer(int id, int userId);

    List<Transfer> list(Account account);

    Account getAccountByUserId(int userId);

    void transfer(Transfer transfer);

    public void setTransfer(Transfer transfer);
}
