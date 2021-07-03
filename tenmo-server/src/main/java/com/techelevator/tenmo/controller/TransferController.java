package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

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


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer transfer(@RequestBody Transfer transfer) {
        transferDao.transfer(transfer);
        return transfer;
    }

    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public Transfer[] list(Principal user) {
         int userId = userDao.findIdByUsername(user.getName());
        Account account = transferDao.getAccountByUserId(userId);
        List<Transfer> transferList = transferDao.list(account);
        Transfer[] transferArray = new Transfer[transferList.size()];
        transferArray = transferList.toArray(transferArray);
        return transferArray;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable int id) {
        Transfer transfer = transferDao.getTransfer(id);
        return transfer;
    }
}