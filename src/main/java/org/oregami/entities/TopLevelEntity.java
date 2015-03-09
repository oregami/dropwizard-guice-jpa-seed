package org.oregami.entities;

/**
 * Created by sebastian on 09.03.15.
 */
public interface TopLevelEntity {

    public enum Discriminator {
        TASK,
        LANGUAGE
    }

    public Discriminator getDiscriminator();

}
