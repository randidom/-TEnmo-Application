package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {


    private TransferDao transferDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }


    @RequestMapping(path = "user/account/transfer", method = RequestMethod.GET)
    public List<Transfer> listTransfers(Principal principal) {
        String currentUsername = principal.getName();
        List<Transfer> allTransfers = transferDao.getAllTransfers();
        List<Transfer> userTransfers = new ArrayList<>();

        //This will only let the logged in user see transfers in which they received/sent
        for (Transfer transfer : allTransfers) {
            if (transfer.getSenderUser().equals(currentUsername) ||
                    transfer.getReceiverUser().equals(currentUsername)) {
                userTransfers.add(transfer);
            }
        }
        return userTransfers;
    }


    @RequestMapping(path = "user/account/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id) {
        Transfer transfer = transferDao.getTransferByID(id);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        } else {
            return transfer;
        }

    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "user/account/transfer", method = RequestMethod.POST)
    public Transfer createATransfer(@RequestBody @Valid Transfer transfer, Principal principal) {
        return transferDao.createATransfer(transfer, principal.getName());


    }

}




