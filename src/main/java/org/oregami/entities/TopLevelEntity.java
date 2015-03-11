package org.oregami.entities;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by sebastian on 11.03.15.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TopLevelEntity {

    public enum Discriminator {
        TASK,
        LANGUAGE
    }

    @NotNull
    Discriminator discriminator();

}
