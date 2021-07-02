package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer getTransfer(int id);

    List<Transfer> list(Account account);

    Account getAccountByUserId(int userId);

    void transfer(Transfer transfer);

//    public Transfer getTransfer(BigDecimal amountToTransfer, int fromUser, int toUser, String statusDesc, String typeDesc);

//    void setTransferType(Transfer transfer);
//
//    void setTransferStatus(Transfer transfer);

    public void setTransfer(Transfer transfer);
}
