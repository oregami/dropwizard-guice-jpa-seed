package org.oregami.service;

import org.oregami.dropwizard.User;

/**
 * Created by sebastian on 04.03.15.
 */
public class ServiceCallContext {

    public ServiceCallContext() {

    }

    public ServiceCallContext(User user) {
        this.user = user;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
