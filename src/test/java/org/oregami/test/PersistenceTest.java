package org.oregami.test;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.*;
import org.oregami.data.DatabaseFiller;
import org.oregami.data.RevisionInfo;
import org.oregami.dropwizard.ToDoApplication;
import org.oregami.entities.*;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.oregami.util.StartHelper;

public class PersistenceTest {

	public PersistenceTest() {
	}

    @BeforeClass
    public static void initClass() {
        StartHelper.init(StartHelper.CONFIG_FILENAME_TEST);
    }

    @AfterClass
    public static void finish() {
        StartHelper.getInstance(DatabaseFiller.class).dropAllData();
    }


    @Test
	public void testTask() {
		TaskDao taskDao = StartHelper.getInstance(TaskDao.class);

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
        TaskDao taskDao = StartHelper.getInstance(TaskDao.class);

        Task t1 = new Task("task 1");
        String id1 = taskDao.save(t1);
        Assert.assertNotNull("ID expected", id1);


        Task t1Loaded = taskDao.findOne(id1);
        Assert.assertNotNull(t1Loaded);
        Assert.assertEquals(t1Loaded.getId(), id1);
        Assert.assertEquals(t1Loaded, t1);

        t1Loaded.setDescription("update");
        taskDao.update(t1Loaded);


        List<RevisionInfo> revisions = taskDao.findRevisions(id1);
        Assert.assertNotNull(revisions);
        Assert.assertEquals(2, revisions.size());

    }



    @Test
    public void testSubTask() {
        TaskDao taskDao = StartHelper.getInstance(TaskDao.class);

        List<Task> all = taskDao.findAll();
        int size = all.size();

        Task t1 = new Task("task 1");

        SubTask s1 = new SubTask();
        s1.setDescription("subtask1");
        t1.addSubTask(s1);

        String id1 = taskDao.save(t1);
        Assert.assertNotNull("ID expected", id1);
        String sid1 = s1.getId();

        all = taskDao.findAll();
        Assert.assertTrue((size+1) + " Tasks expected", all.size()==size+1);

        Task taskLoaded = taskDao.findOne(id1);
        Assert.assertNotNull(taskLoaded);
        Assert.assertNotNull(taskLoaded.getSubTasks());
        Assert.assertEquals(1, taskLoaded.getSubTasks().size());
        SubTask s1loaded = taskLoaded.getSubTasks().iterator().next();
        Assert.assertEquals(sid1, s1loaded.getId());

        System.out.println(taskLoaded);
        System.out.println(s1loaded);

    }


    @Test
    public void testMultipleSubTask() {
        TaskDao taskDao = StartHelper.getInstance(TaskDao.class);
        LanguageDao languageDao = StartHelper.getInstance(LanguageDao.class);

        List<Task> all = taskDao.findAll();
        int size = all.size();

        Language de = new Language(Language.GERMAN);
        de.setDescription("description de1");
        languageDao.save(de);

        Task t1 = new Task("task 1");
        t1.setLanguage(de);

        SubTask s1 = new SubTask();
        s1.setDescription("subtask1");
        t1.addSubTask(s1);

        SubTask s2 = new SubTask();
        s2.setDescription("subtask2");
        t1.addSubTask(s2);

        String id1 = taskDao.save(t1);
        Assert.assertNotNull("ID expected", id1);
        String sid1 = s1.getId();
        String sid2 = s2.getId();

        all = taskDao.findAll();
        Assert.assertTrue((size+1) + " Tasks expected", all.size()==size+1);

        Task taskLoaded = taskDao.findOne(id1);
        Assert.assertNotNull(taskLoaded);
        Assert.assertNotNull(taskLoaded.getSubTasks());
        Assert.assertEquals(2, taskLoaded.getSubTasks().size());
        Assert.assertNotNull(taskLoaded.getLanguage());

        List<String> sidList = new ArrayList<>();
        sidList.add(sid1);
        sidList.add(sid2);

        Iterator<SubTask> subTaskIterator = taskLoaded.getSubTasks().iterator();
        while (subTaskIterator.hasNext()) {
            SubTask subTask = subTaskIterator.next();
            Assert.assertTrue(sidList.contains(subTask.getId()));
            sidList.remove(subTask.getId());
        }

    }


    @Test
    public void testLanguage() {
        LanguageDao languageDao = StartHelper.getInstance(LanguageDao.class);
        Language eng = new Language(Language.ENGLISH);
        eng.setDescription("description eng1");
        languageDao.save(eng);

        Language de = new Language(Language.GERMAN);
        de.setDescription("description de1");
        languageDao.save(de);

        List<Language> all = languageDao.findAll();

        Assert.assertFalse(all.isEmpty());
        Assert.assertEquals(2, all.size());

    }


}
