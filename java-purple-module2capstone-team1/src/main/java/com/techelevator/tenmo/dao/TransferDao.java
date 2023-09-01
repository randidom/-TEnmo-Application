package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

public interface TransferDao {
    List<Transfer> getAllTransfers();

    Transfer getTransferByID(int id);

    Transfer createATransfer(Transfer transfer, String username);





}
