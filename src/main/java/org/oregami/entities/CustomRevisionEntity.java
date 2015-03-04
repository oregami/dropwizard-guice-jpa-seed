package org.oregami.entities;

import org.hibernate.envers.DefaultRevisionEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by sebastian on 04.03.15.
 */
@Entity
@org.hibernate.envers.RevisionEntity(CustomRevisionListener.class)
@Table
public class CustomRevisionEntity extends DefaultRevisionEntity {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
