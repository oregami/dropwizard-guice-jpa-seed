package org.oregami.service;


import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sebastian on 21.11.14.
 */
public class TestUserService {

  @Test
  public void checkWrongCredentials() {
    Assert.assertFalse(UserServiceFake.getInstance().checkCredentials("test", "test"));
  }

  @Test
  public void checkCorrectCredentials() {
    Assert.assertTrue(UserServiceFake.getInstance().checkCredentials("user1", "password1"));
    Assert.assertTrue(UserServiceFake.getInstance().checkCredentials("user2", "password2"));
    Assert.assertTrue(UserServiceFake.getInstance().checkCredentials("admin", "password"));
  }
}
