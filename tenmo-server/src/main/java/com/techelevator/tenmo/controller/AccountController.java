package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/accounts")
public class AccountController {

    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/{accountId}/balance", method =  RequestMethod.GET)
    public BigDecimal balance(@PathVariable int accountId, Principal user) throws AccountNotFoundException {
        String userName = user.getName();
        int userId = userDao.findIdByUsername(userName);
        return accountDao.getAccount(accountId, userId).getBalance();
    }

    // Work on this method next! Need to complete transfer dao set up before hand.
//    @RequestMapping(path = "/{accountId}/transfer", method = RequestMethod.PUT)
//    public void transfer(@PathVariable int accountId, Principal fromUser) throws AccountNotFoundException {
//        String userName = fromUser.getName();
//        int userId = userDao.findIdByUsername(userName);
//
//        transfer();
//    }


}
