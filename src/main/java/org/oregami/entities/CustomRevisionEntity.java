package org.oregami.entities;

import org.hibernate.envers.DefaultTrackingModifiedEntitiesRevisionEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by sebastian on 04.03.15.
 */
@Entity
@org.hibernate.envers.RevisionEntity(CustomRevisionListener.class)
@Table
public class CustomRevisionEntity extends DefaultTrackingModifiedEntitiesRevisionEntity {


    public enum TopLevelEntity {
        TASK,
        LANGUAGE
    }

    private String userId;

    private String entityId;

    private TopLevelEntity entityDiscriminator;

    public TopLevelEntity getEntityDiscriminator() {
        return entityDiscriminator;
    }

    public void setEntityDiscriminator(TopLevelEntity entityDiscriminator) {
        this.entityDiscriminator = entityDiscriminator;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
