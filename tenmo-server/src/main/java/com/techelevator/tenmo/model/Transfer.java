package com.techelevator.tenmo.model;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class Transfer {

    private int transferId;

    @NotEmpty
    @NotNull
    @Min(1)
    @Max(2)
    private int typeId;
    @NotEmpty
    @NotNull
    private String typeDesc;
    @NotEmpty
    @NotNull
    @Min(1)
    @Max(3)
    private int statusId;
    @NotEmpty
    @NotNull
    private String statusDesc;
    @NotEmpty
    @NotNull
    private int toUserId;
    @NotEmpty
    @NotNull
    private int fromUserId;
    @NotEmpty
    @NotNull
    @DecimalMin("0")
    private BigDecimal amount;
    @NotEmpty
    @NotNull
    private String toUserName;
    @NotEmpty
    @NotNull
    private String fromUserName;

    public Transfer(int typeId, String typeDesc, int statusId, String statusDesc, int toUserId, int fromUserId, BigDecimal amount, String toUserName, String fromUserName) {

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

    public Transfer(int transferId) {
        this.transferId = transferId;
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
