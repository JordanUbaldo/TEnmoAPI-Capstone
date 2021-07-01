package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    public void addToBalance(BigDecimal amountToAdd, int to);

    public void subtractToBalance(BigDecimal amountToSubtract, int from);

    Account getAccount(int userId);

}
