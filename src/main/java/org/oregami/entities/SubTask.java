package org.oregami.entities;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;

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
