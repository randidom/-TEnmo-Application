package com.techelevator.dao;


import com.techelevator.tenmo.dao.JbdcUsernameDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Username;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests{

    private JdbcUserDao sut;
    private JbdcUsernameDao sutUser;
    private User userTest;

    private  Username test;


    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
        sutUser = new JbdcUsernameDao(jdbcTemplate);

    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }

    @Test
    public void findUser_by_username_returns_correct_user(){
        User userTester = sut.findByUsername("bob");
        Assert.assertEquals("bob", userTester.getUsername());

        User userTester2 = sut.findByUsername("user");
        Assert.assertEquals("user", userTester2.getUsername());

    }

    @Test
    public void findId_by_username_returns_correct_user(){
        int expected = 1001;
        int actual = sut.findIdByUsername("bob");
        Assert.assertEquals(expected, actual, 0.01);

        int expected2 = 1002;
        int actual2 = sut.findIdByUsername("user");
        Assert.assertEquals(expected2, actual2);
    }

    @Test
    public void returns_all_users(){
        List<User> users = sut.findAll();
        Assert.assertEquals(2, users.size());

    }
    @Test
    public void username_check(){
        boolean userCheck = sut.checkUsername("bob");
        Assert.assertTrue(userCheck);

        boolean userCheck2 = sut.checkUsername("user");
        Assert.assertTrue(userCheck2);


    }

    @Test
    public void listAllUsernames(){
        List<Username> usernames = sutUser.getAllUsernames();
        Assert.assertEquals(2, usernames.size());
    }



    private void assertUsersMatch (User expected, User actual){
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
    }

}
