package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    public void transfer(BigDecimal amountToTransfer, int from, int to, int fromUserId, int toUserId);

    public void addToBalance(BigDecimal amountToAdd, int to, int userId);

    public void subtractToBalance(BigDecimal amountToSubtract, int from, int userId);

    Account getAccount(int accountId, int userId);

}
