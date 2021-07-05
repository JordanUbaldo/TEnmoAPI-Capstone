package com.techelevator;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.exceptions.NsfException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class jdbcTransferTests extends tenmoDaoTests{

private static final Transfer TRANSFER_1 = new Transfer(2,"Send",2,"Approved",1002,1001,new BigDecimal(1),"tom123","bob123");
    private static final Transfer TRANSFER_2 = new Transfer(2,"Send",2,"Approved",1002,1001,new BigDecimal(999999),"tom123","bob123");


    private Transfer testTransfer;

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        sut = new JdbcTransferDao(dataSource);
        testTransfer = new Transfer();
    }

    @Test
    public void test_transfer_is_accurate() {
        BigDecimal expectedFromBalance = sut.getAccountByUserId(1001).getBalance().subtract(new BigDecimal(1));
        BigDecimal expectedToBalance = sut.getAccountByUserId(1002).getBalance().add(new BigDecimal(1));

        sut.transfer(TRANSFER_1);

        BigDecimal actualToBalance = sut.getAccountByUserId(1002).getBalance();
        BigDecimal actualFromBalance = sut.getAccountByUserId(1001).getBalance();
        Assert.assertEquals(expectedFromBalance,actualFromBalance);
        Assert.assertEquals(expectedToBalance,actualToBalance);



    }

    @Test(expected = NsfException.class)
    public void test_transfer_NSF() {
        BigDecimal expectedFromBalance = sut.getAccountByUserId(1001).getBalance();
        BigDecimal expectedToBalance = sut.getAccountByUserId(1002).getBalance();

            sut.transfer(TRANSFER_2);

        BigDecimal actualFromBalance = sut.getAccountByUserId(1001).getBalance();
        BigDecimal actualToBalance = sut.getAccountByUserId(1002).getBalance();

        Assert.assertEquals(expectedFromBalance,actualFromBalance);
        Assert.assertEquals(expectedToBalance,actualToBalance);

    }
    @Test
    public void get_account_by_accountId() {
       Account expected =sut.getAccountByAccountId(2001);

       int actual = expected.getAccountId();
       Assert.assertEquals(2001,actual);


    }


}
