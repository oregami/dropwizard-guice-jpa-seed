package org.oregami.test;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditCriterion;
import org.junit.*;
import org.oregami.dropwizard.ToDoApplication;
import org.oregami.entities.Task;
import org.oregami.entities.TaskDao;

import javax.persistence.EntityManager;
import java.util.List;

public class AuditTest {

	private static Injector injector;

	static EntityManager entityManager = null;

	public AuditTest() {
	}
	
	@BeforeClass
	public static void init() {
		JpaPersistModule jpaPersistModule = new JpaPersistModule(ToDoApplication.JPA_UNIT);
		injector = Guice.createInjector(jpaPersistModule);
		injector.getInstance(AuditTest.class);
		PersistService persistService = injector.getInstance(PersistService.class);
		persistService.start();
        entityManager = injector.getInstance(EntityManager.class);
	}
	
	private <T> T getInstance(Class<T> c) {
		return injector.getInstance(c);
	}

	
	@Test
	public void testTask() {

        entityManager.getTransaction().begin();
		TaskDao taskDao = getInstance(TaskDao.class);
		
		Task t1 = new Task("task 1");
		String id1 = taskDao.save(t1);
		Assert.assertNotNull("ID expected", id1);
		
		List<Task> all = taskDao.findAll();
		Assert.assertTrue("1 Task expected", all.size()==1);

		Task t1Loaded = taskDao.findOne(id1);
		Assert.assertNotNull(t1Loaded);
		Assert.assertEquals(t1Loaded.getId(), id1);
		Assert.assertEquals(t1Loaded, t1);
        entityManager.getTransaction().commit();


        entityManager.getTransaction().begin();
        t1Loaded.setName("updated description");
        taskDao.update(t1Loaded);
        entityManager.getTransaction().commit();



        entityManager.getTransaction().begin();
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        auditReader.createQuery()
                .forEntitiesAtRevision(Task.class, 1).getResultList();
        entityManager.getTransaction().commit();
	}
	
	
}
