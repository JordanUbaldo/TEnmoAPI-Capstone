package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account extends Transfer {

    private int accountId;
    private int userId;
    private BigDecimal balance;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void transfer(BigDecimal amountToTransfer, Account from, Account to) {
        BigDecimal fromBalance = from.getBalance().subtract(amountToTransfer);
        from.setBalance(fromBalance);

        BigDecimal toBalance = to.getBalance().add(amountToTransfer);
        to.setBalance(toBalance);
    }
}
