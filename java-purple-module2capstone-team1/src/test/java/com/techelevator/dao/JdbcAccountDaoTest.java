package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTest extends BaseDaoTests{

    private static final Account ACCOUNT_1 = new Account( 1001, new BigDecimal("1000.00"));
    private static final Account ACCOUNT_2 = new Account( 1002, new BigDecimal("1000.00"));

    private JdbcAccountDao sut;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
       sut = new JdbcAccountDao(jdbcTemplate);

    }

    @Test
    public void get_correct_balance(){
        BigDecimal expectedBalance = ACCOUNT_1.getBalance();
        BigDecimal actualBalance = sut.getAccountBalance("user"); // Replace with an actual username
        Assert.assertEquals(expectedBalance, actualBalance);

        BigDecimal expectedBalance2 = ACCOUNT_2.getBalance();
        BigDecimal actualBalance2 = sut.getAccountBalance("bob");
        Assert.assertEquals(expectedBalance2, actualBalance2);

    }




}
