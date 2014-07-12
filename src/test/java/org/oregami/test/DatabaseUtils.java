package org.oregami.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.oregami.dropwizard.ToDoApplication;

import javax.persistence.EntityManager;

/**
 * Created by sebastian on 09.07.14.
 */
public class DatabaseUtils {

    public static void clearDatabaseTables() {

        Injector injector = Guice.createInjector(new JpaPersistModule(ToDoApplication.JPA_UNIT));
        PersistService persistService = injector.getInstance(PersistService.class);
        persistService.start();

        EntityManager entityManager = injector.getInstance(EntityManager.class);

        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("TRUNCATE TABLE Task").executeUpdate();
        entityManager.getTransaction().commit();

    }

}
