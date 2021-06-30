package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getBalance(int id);

    public void transfer(BigDecimal amountToTransfer, int from, int to);

}
