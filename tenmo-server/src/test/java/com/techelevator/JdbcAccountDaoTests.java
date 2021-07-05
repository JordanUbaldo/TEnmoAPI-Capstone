package com.techelevator;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests  extends tenmoDaoTests {

    private static final Account ACCOUNT_1 = new Account(2003,1003,new BigDecimal(101));



    private Account testAccount;
    private JdbcAccountDao sut;

    @Before
    public void setup() {
        sut = new JdbcAccountDao(dataSource);
        testAccount = new Account();
    }

    @Test
    public void getAccountId_returns_correct_AccountId() {
        Account account = sut.getAccount(1001);
        assertAccountsMatch(ACCOUNT_1, account);

        account = sut.getAccount(1002);
        assertAccountsMatch(ACCOUNT_1,account);
    }

    @Test
    public void getAccount_returns_null_when_AccountId_Incorrect() {
        Account account = sut.getAccount(999);
        Assert.assertNull(account);
    }




    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccountId(),actual.getAccountId());
        Assert.assertEquals(expected.getUserId(),actual.getUserId());

    }
}
