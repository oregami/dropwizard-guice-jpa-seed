package org.oregami.dropwizard;

public class User {

  public User(String username) {
    this.username = username;
  }

  String username;

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
