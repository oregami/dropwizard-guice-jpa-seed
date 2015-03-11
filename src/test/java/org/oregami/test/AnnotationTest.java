package org.oregami.test;

import org.junit.Assert;
import org.junit.Test;
import org.oregami.entities.SubTask;
import org.oregami.entities.Task;
import org.oregami.entities.TopLevelEntity;

/**
 * Created by sebastian on 11.03.15.
 */
public class AnnotationTest {

    @Test
    public void annotationIsPresent() {
        Task t = new Task();

        boolean annotationPresent = t.getClass().isAnnotationPresent(TopLevelEntity.class);
        Assert.assertTrue(annotationPresent);
        Assert.assertEquals(t.getClass().getAnnotation(TopLevelEntity.class).discriminator(), TopLevelEntity.Discriminator.TASK);
    }

    @Test
    public void annotationIsNotPresent() {
        SubTask s = new SubTask();
        boolean annotationPresent = s.getClass().isAnnotationPresent(TopLevelEntity.class);
        Assert.assertFalse(annotationPresent);
    }
}
