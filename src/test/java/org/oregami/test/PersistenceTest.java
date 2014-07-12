package org.oregami.test;


import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oregami.dropwizard.ToDoApplication;
import org.oregami.entities.Task;
import org.oregami.entities.TaskDao;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class PersistenceTest {

	private static Injector injector;
	
	EntityManager entityManager = null;
	
	public PersistenceTest() {
	}
	
	@BeforeClass
	public static void init() {
		JpaPersistModule jpaPersistModule = new JpaPersistModule(ToDoApplication.JPA_UNIT);
		injector = Guice.createInjector(jpaPersistModule);
		injector.getInstance(PersistenceTest.class);
		PersistService persistService = injector.getInstance(PersistService.class);
		persistService.start();
	}
	
	@Before
	public void startTx() {
		if (entityManager==null) {
			entityManager = injector.getInstance(EntityManager.class);
		}
		entityManager.getTransaction().begin();
		
	}
	
	@After
	public void rollbackTx() {
		entityManager.getTransaction().rollback();
	}
	
	private <T> T getInstance(Class<T> c) {
		return injector.getInstance(c);
	}

	
	@Test
	public void testTask() {
		TaskDao taskDao = getInstance(TaskDao.class);
		
		Task t1 = new Task("task 1");
		String id1 = taskDao.save(t1);
		Assert.assertNotNull("ID expected", id1);
		
		List<Task> all = taskDao.findAll();
		Assert.assertTrue("1 Task expected", all.size()==1);
		
		Task t2 = new Task("task 2");
		String id2 = taskDao.save(t2);
		
		all = taskDao.findAll();
		Assert.assertTrue("2 Tasks expected", all.size()==2);
		
		Task t1Loaded = taskDao.findOne(id1);
		Assert.assertNotNull(t1Loaded);
		Assert.assertEquals(t1Loaded.getId(), id1);
		Assert.assertEquals(t1Loaded, t1);
		
		Task t2Loaded = taskDao.findOne(id2);
		Assert.assertNotNull(t2Loaded);
		Assert.assertEquals(t2Loaded.getId(), id2);
		Assert.assertEquals(t2Loaded, t2);		
	}


    @Test
    public void testTaskRevisions() {
        TaskDao taskDao = getInstance(TaskDao.class);

        Task t1 = new Task("task 1");
        String id1 = taskDao.save(t1);
        Assert.assertNotNull("ID expected", id1);

        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();

        Task t1Loaded = taskDao.findOne(id1);
        Assert.assertNotNull(t1Loaded);
        Assert.assertEquals(t1Loaded.getId(), id1);
        Assert.assertEquals(t1Loaded, t1);

        t1Loaded.setDescription("update");
        taskDao.update(t1Loaded);

        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();

        List<Number> revisions = taskDao.findRevisions(id1);
        System.out.println(revisions);
        Assert.assertNotNull(revisions);
        Assert.assertEquals(2, revisions.size());

        DatabaseUtils.clearDatabaseTables();
    }
	
	
}
