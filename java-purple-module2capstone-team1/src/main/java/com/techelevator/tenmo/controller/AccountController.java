package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
    private AccountDao accountDao;


    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;

    }

    @RequestMapping(path = "/user/account", method = RequestMethod.GET)
    public Map<String, Object> accountBalance(Principal principal){
        Map<String, Object> response = new LinkedHashMap<>(); //  Using LinkedHashMap to maintain insertion order
        BigDecimal balance = accountDao.getAccountBalance(principal.getName());

        response.put("username", principal.getName());
        response.put("balance", balance);

        return response;


    }



}
