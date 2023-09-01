package com.techelevator.tenmo.model;

import com.techelevator.tenmo.dao.AccountDao;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.security.Principal;

public class Transfer {

    private int transferId;

    private BigDecimal transferAmount;

@NotBlank
    private String receiverUser;

    private String senderUser;

    private String transferStatus = "Approved";

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(String receiverUser) {
        this.receiverUser = receiverUser;
    }

    public String getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(String senderUser) {
        this.senderUser = senderUser;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Transfer(int transferId, BigDecimal transferAmount, String receiverUser, String senderUser, String transferStatus) {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.receiverUser = receiverUser;
        this.senderUser = senderUser;
        this.transferStatus = transferStatus;
    }

    public Transfer(BigDecimal transferAmount, String receiverUser, String senderUser, String transferStatus) {
        this.transferAmount = transferAmount;
        this.receiverUser = receiverUser;
        this.senderUser = senderUser;
        this.transferStatus = transferStatus;
    }

    public Transfer(){

    }




}
