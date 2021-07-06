package com.techelevator;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class jdbcUserDaoTests extends tenmoDaoTests{
        private static  User USER_1 = new User(1001,"bob123");

    private JdbcUserDao sut;

    private User testUser;


    @Before
    public void setup() {
        sut = new JdbcUserDao(dataSource);
        testUser = new User();
    }

    @Test
    public void test_create(){
        sut.create("abc123", "abc123");
        User user = sut.findByUsername("abc123");
         String actual = user.getUsername();
         String expected = "abc123";
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getUser_returns_username() {
        User user = sut.findByUsername("bob123");
        assertUserMatch(USER_1, user);
    }

    @Test
    public void getUserId_by_Username() {
        int actualUserId = sut.findIdByUsername("bob123");

        int expectedUserId = USER_1.getId();
        Assert.assertEquals(expectedUserId, actualUserId);
    }

    @Test
    public void find_All_users_return_users() {
        List<User> actualResults = sut.findAll();
        for (User user : actualResults) {
            System.out.println(user.getId() + " " + user.getUsername());
        }

        User actual = actualResults.get(0);

        assertUserMatch(USER_1,actual);
    }

    private void assertUserMatch(User expected, User actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());

    }
}
