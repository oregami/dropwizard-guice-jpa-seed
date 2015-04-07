package org.oregami.data;

import org.apache.log4j.Logger;
import org.oregami.dropwizard.ToDoApplication;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.oregami.entities.Language;
import org.oregami.entities.LanguageDao;
import org.oregami.entities.SubTask;
import org.oregami.entities.Task;
import org.oregami.service.LanguageService;
import org.oregami.service.ServiceResult;
import org.oregami.service.TaskService;
import org.oregami.util.StartHelper;

import javax.persistence.EntityManager;

/**
 * Class to fill the database with some sample entities.
 */
public class DatabaseFiller {

	Logger logger = Logger.getLogger(DatabaseFiller.class);

	static DatabaseFiller instance = null;

	@Transactional
	public void initData() {
		addLanguages();
        addTasks();
	}


	private void addLanguages() {
        LanguageService languageService = StartHelper.getInstance(LanguageService.class);
        Language english = new Language("english");
        english.setDescription("This is the english language");
        ServiceResult<Language> languageServiceResult = languageService.createNewEntity(english, null);
        if (languageServiceResult.hasErrors()) {
            throw new RuntimeException(languageServiceResult.getErrors().toString());
        }

        Language german = new Language("german");
        german.setDescription("This is the german language");
        languageServiceResult = languageService.createNewEntity(german, null);
        if (languageServiceResult.hasErrors()) {
            throw new RuntimeException(languageServiceResult.getErrors().toString());
        }
    }

    private void addTasks() {
        TaskService taskService = StartHelper.getInstance(TaskService.class);
        LanguageDao languageDao = StartHelper.getInstance(LanguageDao.class);
        Task t1 = new Task("task 1");
        t1.setDescription("This is task 1");
        t1.setLanguage(languageDao.findByExactName("english"));

        SubTask sub1 = new SubTask();
        sub1.setDescription("this a subtask 1");
        t1.addSubTask(sub1);

        ServiceResult<Task> taskServiceResult = taskService.createNewEntity(t1, null);
        if (taskServiceResult.hasErrors()) {
            throw new RuntimeException(taskServiceResult.getErrors().toString());
        }

        Task t2 = new Task("task 2");
        t2.setDescription("This is task 2");
        t2.setLanguage(languageDao.findByExactName("english"));
        taskServiceResult = taskService.createNewEntity(t2, null);
        if (taskServiceResult.hasErrors()) {
            throw new RuntimeException(taskServiceResult.getErrors().toString());
        }

    }

    @Transactional
    public void dropAllData() {
        EntityManager em = StartHelper.getInstance(EntityManager.class);
        em.createNativeQuery("TRUNCATE SCHEMA public AND COMMIT NO CHECK").executeUpdate();
    }
}
