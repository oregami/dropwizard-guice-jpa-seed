package org.oregami.test;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oregami.dropwizard.ToDoApplication;
import org.oregami.entities.Task;
import org.oregami.service.FieldNames;
import org.oregami.service.ServiceError;
import org.oregami.service.ServiceErrorContext;
import org.oregami.service.ServiceErrorMessage;
import org.oregami.util.StartHelper;
import org.oregami.validation.TaskValidator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

import javax.persistence.EntityManager;

public class TestTaskValidator {

	ServiceError errorNameTooShort = new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_TOO_SHORT);
	ServiceError errorNameEmpty = new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_EMPTY);
	ServiceError errorDescriptionEmpty = new ServiceError(new ServiceErrorContext(FieldNames.TASK_DESCRIPTION), ServiceErrorMessage.TASK_DESCRIPTION_EMPTY);

    @BeforeClass
    public static void initClass() {
        StartHelper.init(StartHelper.CONFIG_FILENAME_TEST);
    }

	@Test
	public void testNameEmpty() {
		Task t = new Task(null);
		t.setDescription("This is a description");

		TaskValidator validator = new TaskValidator(t);

		List<ServiceError> errors = validator.validateForCreation();
		System.out.println(errors.toString());

		Assert.assertFalse(errors.isEmpty());
		Assert.assertEquals(1, errors.size());

		Assert.assertTrue(errors.contains(errorNameEmpty));
		Assert.assertFalse(errors.contains(errorNameTooShort));
		Assert.assertFalse(errors.contains(errorDescriptionEmpty));

	}

	@Test
	public void testNoErrors() {
		Task t = new Task("valid task name");
		t.setDescription("This is a description");

		TaskValidator validator = new TaskValidator(t);

		List<ServiceError> errors = validator.validateForCreation();
		System.out.println(errors.toString());

		Assert.assertTrue(errors.isEmpty());
		Assert.assertEquals(0, errors.size());

	}

	@Test
	public void testNameTooShort() {
		Task t = new Task("aa");
		t.setDescription("This is a description");

		TaskValidator validator = new TaskValidator(t);

		List<ServiceError> errors = validator.validateForCreation();
		System.out.println(errors.toString());

		Assert.assertFalse(errors.isEmpty());
		Assert.assertEquals(1, errors.size());

		Assert.assertFalse(errors.contains(errorNameEmpty));
		Assert.assertFalse(errors.contains(errorDescriptionEmpty));
		Assert.assertTrue(errors.contains(errorNameTooShort));

	}

	@Test
	public void testDescriptionTooShort() {
		Task t = new Task("Task name");

		TaskValidator validator = new TaskValidator(t);

		List<ServiceError> errors = validator.validateForCreation();
		System.out.println(errors.toString());

		Assert.assertFalse(errors.isEmpty());
		Assert.assertEquals(1, errors.size());

		Assert.assertFalse(errors.contains(errorNameEmpty));
		Assert.assertFalse(errors.contains(errorNameTooShort));
		Assert.assertTrue(errors.contains(errorDescriptionEmpty));

	}



}
