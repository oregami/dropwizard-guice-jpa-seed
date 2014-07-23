package org.oregami.entities;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.PreUpdate;

@Entity
@Audited
public class SubTask extends BaseEntityUUID {

    public SubTask() {};

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
