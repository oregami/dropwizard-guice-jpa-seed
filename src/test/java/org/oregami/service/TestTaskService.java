package org.oregami.service;

import org.junit.*;
import org.oregami.data.DatabaseFiller;
import org.oregami.dropwizard.User;
import org.oregami.entities.Task;
import org.oregami.util.StartHelper;

import javax.persistence.EntityManager;

/**
 * Created by sebastian on 25.08.14.
 */
public class TestTaskService {

    private EntityManager entityManager;

    @BeforeClass
    public static void initClass() {
        StartHelper.init(StartHelper.CONFIG_FILENAME_TEST);
    }

    @AfterClass
    public static void finish() {
        StartHelper.getInstance(DatabaseFiller.class).dropAllData();
    }

    @Before
    public void startTx() {
        if (entityManager==null) {
            entityManager = StartHelper.getInstance(EntityManager.class);
        }
        entityManager.getTransaction().begin();

    }

    @After
    public void rollbackTx() {
        entityManager.getTransaction().commit();
    }

    @Test
    public void saveTaskWithoutError() {
        TaskService service = StartHelper.getInstance(TaskService.class);

        Task t = new Task("name");
        t.setDescription("this is a description");
        ServiceCallContext context = new ServiceCallContext();
        context.setUser(new User("userId-1"));
        ServiceResult<Task> result = service.createNewEntity(t, context);

        Assert.assertFalse(result.hasErrors());
        Assert.assertEquals(0, result.getErrors().size());

        /*
        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();


        Assert.assertFalse(result.hasErrors());
        Assert.assertEquals(0, result.getErrors().size());
        List<Task> all = dao.findAll();
        Assert.assertEquals(1, all.size());

        Task tLoaded = all.get(0);
        tLoaded.setDescription("updated description");

        service.updateTask(tLoaded);
        */


    }


}
