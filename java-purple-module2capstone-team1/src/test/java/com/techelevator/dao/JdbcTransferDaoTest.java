package com.techelevator.dao;

import com.fasterxml.jackson.databind.JsonSerializable;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public class JdbcTransferDaoTest extends BaseDaoTests {

    private static final Transfer TRANSFER_1 = new Transfer(3001, new BigDecimal("100.00"), "user", "bob", "Approved");
    private static final Transfer TRANSFER_2 = new Transfer(3002, new BigDecimal("150.00"), "user", "bob", "Approved");


    private JdbcTransferDao sut;
    private Transfer transferTest;
    private Principal principal;

    private AccountDao accountDao;


    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        accountDao = new JdbcAccountDao(jdbcTemplate);
         sut = new JdbcTransferDao(jdbcTemplate, accountDao);

    }


    @Test
    public void getAllTransfers (){
        List<Transfer> transfers = sut.getAllTransfers();
        Assert.assertEquals(3, transfers.size());
        //
    }

    @Test
    public void getTransferById_returns_correct_transfer(){
    Transfer transfer = sut.getTransferByID(3001);
    assertTransferMatch(transfer, TRANSFER_1);

    Transfer transfer2 = sut.getTransferByID(3002);
    assertTransferMatch(transfer2, TRANSFER_2);

    }
  @Test
   public void getTransferById_returns_null_when_id_not_found(){
    Transfer transfer = sut.getTransferByID(9999);
       Assert.assertNull(transfer);

        Transfer transfer2 = sut.getTransferByID(100);
        Assert.assertNull(transfer2);

    }

@Test
public void createNewTransfer(){
        Transfer testTransfer = new Transfer(3004, new BigDecimal("100"),"bob", "user", "Approved");
       Transfer createdTransfer = sut.createATransfer(testTransfer, "user");
        int newTransferId = testTransfer.getTransferId();

        Assert.assertTrue(newTransferId > 0);

        Transfer recievedTransfer = sut.getTransferByID(newTransferId);
        assertTransferMatch(createdTransfer, recievedTransfer);
}

    private void assertTransferMatch(Transfer expected, Transfer actual){
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getReceiverUser(), actual.getReceiverUser());
        Assert.assertEquals(expected.getSenderUser(), actual.getSenderUser());
        Assert.assertEquals(expected.getTransferStatus(), actual.getTransferStatus());
        Assert.assertEquals(expected.getTransferAmount(), actual.getTransferAmount());
    }

}
