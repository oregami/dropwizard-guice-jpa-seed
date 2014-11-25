package org.oregami.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Used as a "fake" user service (users are not available in the database)
 */
public class UserServiceFake {

  private static Map<String, String> userRepository = null;

  private static UserServiceFake instance = null;

  public static UserServiceFake getInstance() {
    synchronized (UserServiceFake.class) {
      if (instance == null) {
        instance = new UserServiceFake();
        instance.init();
      }
      return instance;
    }
  }

  private void init() {
    userRepository = new HashMap<>();
    userRepository.put("user1", "password1");
    userRepository.put("user2", "password2");
    userRepository.put("admin", "password");
  }

  /**
   * Returns true, if a user with the given password was found
   * @param username
   * @param unencryptedPassword
   * @return
   */
  public boolean checkCredentials(String username, String unencryptedPassword) {

    boolean ret = false;

    if (userRepository.get(username)!=null && userRepository.get(username).equals(unencryptedPassword)) {
      ret = true;
    }

    return ret;

  }





}
