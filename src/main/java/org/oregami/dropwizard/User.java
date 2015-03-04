package org.oregami.dropwizard;

import java.util.UUID;

public class User {

    public User(String username) {
        this.username = username;
        this.userId = UUID.randomUUID().toString();
    }

    private final String userId;
    String username;

    public String getUserId() {
        return userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
