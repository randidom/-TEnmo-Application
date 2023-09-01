package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dao.UsernameDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Username;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class UserController {
    private UserDao userDao;
    private UsernameDao usernameDao;

    public UserController(UserDao userDao, UsernameDao usernameDao){
        this.userDao = userDao;
        this.usernameDao = usernameDao;
    }
    @RequestMapping(path="/usercheck/{username}", method= RequestMethod.GET)
    public boolean userCheck(@PathVariable String username){
        return userDao.checkUsername(username);
    }


// This method calls to the Username class because this will only output the username of the accounts and not portray any sensitive data (ex.password hash)
    @RequestMapping(path = "/user", method =  RequestMethod.GET)
    public List<Username> getOnlyUsers(){
        return usernameDao.getAllUsernames();
    }


    @RequestMapping(path = "user/{username}", method = RequestMethod.GET)
    public User getUserByUserName(@PathVariable String username) {
        try {
            return userDao.findByUsername(username);
        } catch (RestClientResponseException | ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @RequestMapping(path = "user/{username}/id", method = RequestMethod.GET)
      public int getIdByUser(@PathVariable String username) {
        try {

            return userDao.findIdByUsername(username);
        } catch (RestClientResponseException | ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public boolean createUser(@PathVariable String username, @PathVariable String password){
        try {
            return userDao.create(username, password);
        } catch (RestClientResponseException | ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }




}
