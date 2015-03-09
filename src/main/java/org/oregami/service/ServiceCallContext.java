package org.oregami.service;

import org.oregami.dropwizard.User;
import org.oregami.entities.CustomRevisionEntity;
import org.oregami.entities.TopLevelEntity;

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

    private String entityId;

    private TopLevelEntity.Discriminator entityDiscriminator;

    public TopLevelEntity.Discriminator getEntityDiscriminator() {
        return entityDiscriminator;
    }

    public void setEntityDiscriminator(TopLevelEntity.Discriminator entityDiscriminator) {
        this.entityDiscriminator = entityDiscriminator;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
