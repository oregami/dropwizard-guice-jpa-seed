package org.oregami.test;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.*;
import org.oregami.data.DatabaseFiller;
import org.oregami.dropwizard.ToDoApplication;
import org.oregami.entities.*;
import org.oregami.util.StartHelper;

import javax.persistence.EntityManager;
import java.util.Iterator;
import java.util.List;

public class AuditTest {

	static EntityManager entityManager = null;

	public AuditTest() {
	}

    @BeforeClass
    public static void initClass() {
        StartHelper.init(StartHelper.CONFIG_FILENAME_TEST);
        entityManager = StartHelper.getInstance(EntityManager.class);
    }

    @AfterClass
    public static void finish() {
        StartHelper.getInstance(DatabaseFiller.class).dropAllData();
    }


	@Test
	public void testTask() {

        entityManager.getTransaction().begin();
		TaskDao taskDao = StartHelper.getInstance(TaskDao.class);

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
        Task t1Loaded2 = taskDao.findOne(id1);
        t1Loaded2.setName("updated description");
        taskDao.update(t1Loaded2);
        entityManager.getTransaction().commit();


        entityManager.getTransaction().begin();
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(Task.class,
                t1.getId());
        System.out.println("revisions: " + revisions);

        Assert.assertEquals(2,revisions.size());

        AuditReader reader = AuditReaderFactory.get(entityManager);
        for (Number n : revisions) {
            Task tRev = reader.find(Task.class, t1.getId(), n);
            Assert.assertNotNull(tRev);
            System.out.println("Revision " + n + ": " + tRev);
        }

        entityManager.getTransaction().commit();


        entityManager.getTransaction().begin();
        SubTask s1 = new SubTask();
        s1.setDescription("subtask1");
        t1.addSubTask(s1);
        t1Loaded.addSubTask(s1);
        taskDao.update(t1Loaded);
        entityManager.getTransaction().commit();



        entityManager.getTransaction().begin();
        revisions = auditReader.getRevisions(Task.class,
                t1.getId());
        Assert.assertEquals(3,revisions.size());
        for (Number n : revisions) {
            Task tRev = reader.find(Task.class, t1.getId(), n);
            Assert.assertNotNull(tRev);
            System.out.println("Revision " + n + ": " + tRev);
        }
        entityManager.getTransaction().commit();


        entityManager.getTransaction().begin();
        t1Loaded = taskDao.findOne(id1);
        Iterator<SubTask> subTaskIterator = t1Loaded.getSubTasks().iterator();
        t1Loaded.setDescription("test for subtask update");
        Assert.assertTrue(subTaskIterator.hasNext());
        SubTask subTask = subTaskIterator.next();
        Assert.assertNotNull(subTask);
        subTask.setDescription("subtask 1 updated description");
        taskDao.update(t1Loaded);
        entityManager.getTransaction().commit();


        entityManager.getTransaction().begin();
        revisions = auditReader.getRevisions(Task.class,
                t1.getId());
        Assert.assertEquals(4,revisions.size());
        for (Number n : revisions) {
            Task tRev = reader.find(Task.class, t1.getId(), n);
            Assert.assertNotNull(tRev);
            System.out.println("Revision " + n + ": " + tRev);
        }
        entityManager.getTransaction().commit();


        entityManager.getTransaction().begin();
        t1Loaded = taskDao.findOne(id1);
        taskDao.delete(t1Loaded);
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        all = taskDao.findAll();
        Assert.assertTrue("0 Task expected", all.size() == 0);
        entityManager.getTransaction().commit();


	}


    @Test
    public void testLanguage() {
        LanguageDao languageDao = StartHelper.getInstance(LanguageDao.class);

        entityManager.getTransaction().begin();
        Language en = new Language(Language.ENGLISH);
        en.setDescription("description eng1");
        languageDao.save(en);
        Language de = new Language(Language.GERMAN);
        de.setDescription("description de1");
        languageDao.save(de);
        List<Language> all = languageDao.findAll();
        Assert.assertFalse(all.isEmpty());
        Assert.assertEquals(2, all.size());
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        Language enLoaded = languageDao.findOne(en.getId());
        enLoaded.setDescription("descriptopn en2");
        languageDao.update(enLoaded);
        all = languageDao.findAll();
        Assert.assertFalse(all.isEmpty());
        Assert.assertEquals(2, all.size());
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(Language.class,
                en.getId());
        System.out.println("revisions: " + revisions);
        Assert.assertEquals(2, revisions.size());
        AuditReader reader = AuditReaderFactory.get(entityManager);
        for (Number n : revisions) {
            Language lRev = reader.find(Language.class, en.getId(), n);
            Assert.assertNotNull(lRev);
            System.out.println("Language rev " + n + ": " + lRev);
        }
        entityManager.getTransaction().commit();

        //delete languages from DB:
        entityManager.getTransaction().begin();
        Language enToDelete = languageDao.findOne(en.getId());
        languageDao.delete(enToDelete);
        all = languageDao.findAll();
        Assert.assertFalse(all.isEmpty());
        Assert.assertEquals(1, all.size());
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        Language deLoaded = languageDao.findOne(de.getId());
        languageDao.delete(deLoaded);
        all = languageDao.findAll();
        Assert.assertTrue(all.isEmpty());
        entityManager.getTransaction().commit();

    }


    @Test
    public void testTaskWithChangedLanguage() {


        LanguageDao languageDao = StartHelper.getInstance(LanguageDao.class);

        entityManager.getTransaction().begin();
        Language en = new Language(Language.ENGLISH);
        en.setDescription("description en1");
        languageDao.save(en);
        Language de = new Language(Language.GERMAN);
        de.setDescription("description de1");
        languageDao.save(de);
        List<Language> allLanguages = languageDao.findAll();
        Assert.assertFalse(allLanguages.isEmpty());
        Assert.assertEquals(2, allLanguages.size());
        entityManager.getTransaction().commit();



        entityManager.getTransaction().begin();
        TaskDao taskDao = StartHelper.getInstance(TaskDao.class);

        Task t1 = new Task("task 1");
        t1.setLanguage(en);
        String id1 = taskDao.save(t1);
        Assert.assertNotNull("ID expected", id1);

        List<Task> all = taskDao.findAll();
        Assert.assertTrue("1 Task expected", all.size() == 1);

        Task t1Loaded = taskDao.findOne(id1);
        Assert.assertNotNull(t1Loaded);
        Assert.assertEquals(t1Loaded.getId(), id1);
        Assert.assertEquals(t1Loaded, t1);
        Assert.assertEquals(t1Loaded.getLanguage(), en);
        System.out.println(t1Loaded);
        entityManager.getTransaction().commit();


        entityManager.getTransaction().begin();
        Language enLoaded = languageDao.findOne(en.getId());
        enLoaded.setDescription("descripton en2");
        languageDao.update(enLoaded);
        allLanguages = languageDao.findAll();
        Assert.assertFalse(allLanguages.isEmpty());
        Assert.assertEquals(2, allLanguages.size());
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        t1Loaded = taskDao.findOne(id1);
        enLoaded = languageDao.findOne(en.getId());
        Assert.assertEquals(t1Loaded.getLanguage(), enLoaded);
        Assert.assertEquals(t1Loaded.getLanguage().getDescription(), enLoaded.getDescription());
        System.out.println(t1Loaded);
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        t1Loaded = taskDao.findOne(id1);
        t1Loaded.setLanguage(null);
        taskDao.update(t1Loaded);
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        t1Loaded = taskDao.findOne(id1);
        Assert.assertNull(t1Loaded.getLanguage());
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(Task.class,
                t1Loaded.getId());
        System.out.println("task_revisions: " + revisions);
        Assert.assertEquals(2, revisions.size());
        AuditReader reader = AuditReaderFactory.get(entityManager);
        for (Number n : revisions) {
            Task tRev = reader.find(Task.class, t1Loaded.getId(), n);
            Assert.assertNotNull(tRev);
            System.out.println("Task_rev_" + n + ": " + tRev);
        }
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        taskDao.delete(t1Loaded);
        languageDao.delete(enLoaded);
        languageDao.delete(de);
        entityManager.getTransaction().commit();

    }



}
