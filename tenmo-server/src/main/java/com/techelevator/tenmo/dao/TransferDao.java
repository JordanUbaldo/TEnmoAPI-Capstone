package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer getTransfer(int id);

    List<Transfer> list();

    public void transfer(BigDecimal amountToTransfer, int fromUser, int toUser);

    public Transfer getTransfer(BigDecimal amountToTransfer, int fromUser, int toUser);

    void setTransferType(Transfer transfer, String description);

    void setTransferStatus(Transfer transfer, String description);

    public void setTransfer(Transfer transfer);
}
