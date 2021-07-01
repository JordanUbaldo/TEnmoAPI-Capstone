package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfer")
public class TransferController {

    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer transfer(@RequestBody int toUserId, @RequestBody BigDecimal amount, Principal user) throws AccountNotFoundException {
        String fromUser= user.getName();
        int fromUserId = userDao.findIdByUsername(fromUser);

        transferDao.transfer(amount, fromUserId, toUserId);
        Transfer transfer = transferDao.getTransfer(amount, fromUserId, toUserId);
        transferDao.setTransfer(transfer);

        return transfer;
    }
}