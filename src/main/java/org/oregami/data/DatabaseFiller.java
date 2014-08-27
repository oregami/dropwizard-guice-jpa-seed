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
import org.oregami.entities.Task;
import org.oregami.service.LanguageService;
import org.oregami.service.ServiceError;
import org.oregami.service.ServiceResult;
import org.oregami.service.TaskService;

/**
 * Class to fill the database with some sample entities.
 */
public class DatabaseFiller {

	Logger logger = Logger.getLogger(DatabaseFiller.class);
	
	static DatabaseFiller instance = null;
	
	static Injector injector;
	
	
	@Transactional
	public void initData() {
		addLanguages();
        addTasks();
	}


    public static DatabaseFiller getInstance() {

		if (instance==null) {
			JpaPersistModule jpaPersistModule = ToDoApplication.createJpaModule();
			injector = Guice.createInjector(jpaPersistModule);
			instance = injector.getInstance(DatabaseFiller.class);
			PersistService persistService = injector.getInstance(PersistService.class);
			persistService.start();
		}
		return instance;
	}	

	private void addLanguages() {
        LanguageService languageService = injector.getInstance(LanguageService.class);
        Language english = new Language("english");
        english.setDescription("This is the english language");
        ServiceResult<Language> languageServiceResult = languageService.createNewLanguage(english);
        if (languageServiceResult.hasErrors()) {
            throw new RuntimeException(languageServiceResult.getErrors().toString());
        }
    }

    private void addTasks() {
        TaskService taskService = injector.getInstance(TaskService.class);
        LanguageDao languageDao = injector.getInstance(LanguageDao.class);
        Task t1 = new Task("task1");
        t1.setDescription("This is task 1");
        t1.setLanguage(languageDao.findByExactName("english"));
        ServiceResult<Task> taskServiceResult = taskService.createNewTask(t1);
        if (taskServiceResult.hasErrors()) {
            throw new RuntimeException(taskServiceResult.getErrors().toString());
        }
    }
}
