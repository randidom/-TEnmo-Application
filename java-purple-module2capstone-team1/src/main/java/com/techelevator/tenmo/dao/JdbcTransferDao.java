package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;


    private AccountDao accountDao;


    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;

    }

    @Override
    public List<Transfer> getAllTransfers() {
        List<Transfer> results = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_amount, from_user, to_user, transfer_status FROM transfer";
        try {
            SqlRowSet queryResult = jdbcTemplate.queryForRowSet(sql);
            while (queryResult.next()) {
                Transfer current = mapRowToTransfer(queryResult);
                results.add(current);
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to database or server");
        }
        return results;
    }

    @Override
    public Transfer getTransferByID(int id) {
        Transfer transfer = null;
        String sql = "SELECT transfer_amount, from_user, to_user, transfer_status, transfer_id FROM transfer WHERE transfer_id = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            if (rowSet.next()) {
                transfer = mapRowToTransfer(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to database or server");
        }

        return transfer;
    }

    @Override
    public Transfer createATransfer(Transfer transfer, String username) {
        Transfer newTransfer = new Transfer();

        BigDecimal senderAccount = accountDao.getAccountBalance(username);
        BigDecimal receiverAccount = accountDao.getAccountBalance(transfer.getReceiverUser());

        // These means that if the account balance is greater/equal to the transfer amount AND
        // the transfer amount is greater than zero AND the user is not transferring themselves money
        String sql = "INSERT INTO transfer (transfer_amount, from_user, to_user, transfer_status) " +
                "VALUES (?,?,?,?) RETURNING transfer_id";

        if (senderAccount.compareTo(transfer.getTransferAmount()) >= 0 &&
                transfer.getTransferAmount().compareTo(BigDecimal.ZERO) > 0 &&
                !username.equals(transfer.getReceiverUser())) {

            //This subtracts the transfer money from the sender account and then adds that value of transfer money to the receiver account
          senderAccount= senderAccount.subtract(transfer.getTransferAmount());
            receiverAccount= receiverAccount.add(transfer.getTransferAmount());
            newTransfer.setTransferStatus("Approved");

            //This will also update our balance while it's creating a transfer
            String sqlUpdateBalance = "UPDATE account SET balance = balance + ? WHERE user_id = ?;";
            //Instead of helper method could also do "UPDATE account SET balance = balance + ? WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username =?).

            jdbcTemplate.update(sqlUpdateBalance, transfer.getTransferAmount(), convertUsernameToAccount(transfer.getReceiverUser()));
            jdbcTemplate.update(sqlUpdateBalance, transfer.getTransferAmount().negate(), convertUsernameToAccount(username));

        }  else if (!(senderAccount.compareTo(transfer.getTransferAmount()) >= 0)) {
            System.out.println("Insufficient funds to transfer. You are exceeding the funds in your account");
            newTransfer.setTransferStatus("Rejected");
        } else if (!(transfer.getTransferAmount().compareTo(BigDecimal.ZERO) > 0)) {
            System.out.println("Transfer amount must be greater than zero");
            newTransfer.setTransferStatus("Rejected");
        } else if (username.equals(transfer.getReceiverUser())) {
            System.out.println("You cannot transfer money to yourself!");
            newTransfer.setTransferStatus("Rejected");

        }   try {
            int transferId = jdbcTemplate.queryForObject(sql, int.class, transfer.getTransferAmount(), username, transfer.getReceiverUser(), newTransfer.getTransferStatus());
            newTransfer = getTransferByID(transferId);
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data integrity violation");
        }

            return newTransfer;
        }


        // Helper method to refer back into previous method to declare username and account balance associated with that
    private int convertUsernameToAccount(String username){
        String sql = "SELECT user_id FROM tenmo_user WHERE username = ?";
        int user =jdbcTemplate.queryForObject(sql, int.class, username);
        return user;
    }



    private Transfer mapRowToTransfer(SqlRowSet result){
        Transfer transfer = new Transfer();
        transfer.setReceiverUser(result.getString("to_user"));
        transfer.setSenderUser(result.getString("from_user"));
        transfer.setTransferAmount(result.getBigDecimal("transfer_amount"));
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setTransferStatus(result.getString("transfer_status"));

        return transfer;
    }

}
