package org.oregami.test;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oregami.dropwizard.ToDoApplication;
import org.oregami.entities.Task;
import org.oregami.entities.TaskDao;
import org.oregami.service.FieldNames;
import org.oregami.service.ServiceError;
import org.oregami.service.ServiceErrorContext;
import org.oregami.service.ServiceErrorMessage;
import org.oregami.validation.TaskValidator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class TestTaskValidator {

	static Injector injector = null;
	
	ServiceError errorNameTooShort = new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_TOO_SHORT);
	ServiceError errorNameEmpty = new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_EMPTY);
	
	@BeforeClass
	public static void init() {
		JpaPersistModule jpaPersistModule = new JpaPersistModule(ToDoApplication.JPA_UNIT);
		injector = Guice.createInjector(jpaPersistModule);
		injector.getInstance(PersistenceTest.class);
		PersistService persistService = injector.getInstance(PersistService.class);
		persistService.start();
	}
	
	@Test
	public void testNameEmpty() {
		Task t = new Task(null);
		
		TaskValidator validator = new TaskValidator(injector.getInstance(TaskDao.class), t);
		
		List<ServiceError> errors = validator.validateForCreation();
		System.out.println(errors.toString());
		
		Assert.assertFalse(errors.isEmpty());
		Assert.assertEquals(1, errors.size());
		
		Assert.assertTrue(errors.contains(errorNameEmpty));
		Assert.assertFalse(errors.contains(errorNameTooShort));
		
	}
	
	@Test
	public void testNoErrors() {
		Task t = new Task("valid task name");
		
		TaskValidator validator = new TaskValidator(injector.getInstance(TaskDao.class), t);
		
		List<ServiceError> errors = validator.validateForCreation();
		System.out.println(errors.toString());
		
		Assert.assertTrue(errors.isEmpty());
		Assert.assertEquals(0, errors.size());
		
	}	
	
	public void testNameTooShort() {
		Task t = new Task("aa");
		
		TaskValidator validator = new TaskValidator(injector.getInstance(TaskDao.class), t);
		
		List<ServiceError> errors = validator.validateForCreation();
		System.out.println(errors.toString());
		
		Assert.assertFalse(errors.isEmpty());
		Assert.assertEquals(1, errors.size());
		
		Assert.assertFalse(errors.contains(errorNameEmpty));
		Assert.assertTrue(errors.contains(errorNameTooShort));
		
	}	
	
	
	
}
