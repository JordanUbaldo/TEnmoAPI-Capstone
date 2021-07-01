package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    private int typeId;
    private String typeDesc = "Send";
    private int statusId;
    private String statusDesc = "Approve";
    private int to;
    private int from;
    private BigDecimal amount;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int accountId) {
        this.from = accountId;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int accountId) {
        this.to = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
