package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    private int typeId;
    private String typeDesc;
    private int statusId;
    private String statusDesc;
    private int toUserId;
    private int fromUserId;
    private BigDecimal amount;
    private String toUserName;
    private String fromUserName;

    public Transfer(int transferId, int typeId, String typeDesc, int statusId, String statusDesc, int toUserId, int fromUserId, BigDecimal amount, String toUserName, String fromUserName) {
        this.transferId = transferId;
        this.typeId = typeId;
        this.typeDesc = typeDesc;
        this.statusId = statusId;
        this.statusDesc = statusDesc;
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.amount = amount;
        this.toUserName = toUserName;
        this.fromUserName = fromUserName;
    }

    public Transfer() {
    }

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

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int userId) {
        this.fromUserId = userId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int userId) {
        this.toUserId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }
}
