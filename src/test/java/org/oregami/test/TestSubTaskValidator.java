package org.oregami.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oregami.dropwizard.ToDoApplication;
import org.oregami.entities.SubTask;
import org.oregami.entities.Task;
import org.oregami.service.FieldNames;
import org.oregami.service.ServiceError;
import org.oregami.service.ServiceErrorContext;
import org.oregami.service.ServiceErrorMessage;
import org.oregami.validation.TaskValidator;

import java.util.List;

public class TestSubTaskValidator {

	static Injector injector = null;

	ServiceError errorDescriptionEmpty = new ServiceError(new ServiceErrorContext(FieldNames.SUBTASK_DESCRIPTION), ServiceErrorMessage.SUBTASK_DESCRIPTION_EMPTY);
    ServiceError errorDescriptionTooShort = new ServiceError(new ServiceErrorContext(FieldNames.SUBTASK_DESCRIPTION), ServiceErrorMessage.SUBTASK_DESCRIPTION_TOO_SHORT);

    @BeforeClass
	public static void init() {
		JpaPersistModule jpaPersistModule = new JpaPersistModule(ToDoApplication.JPA_UNIT);
		injector = Guice.createInjector(jpaPersistModule);
		injector.getInstance(PersistenceTest.class);
		PersistService persistService = injector.getInstance(PersistService.class);
		persistService.start();
	}

    private Task createValidTask() {
        Task t = new Task("valid task name");
        t.setDescription("This is a description");
        return t;
    }


    @Test
    public void testNoErrors() {
        Task t = createValidTask();
        SubTask subTask = new SubTask();
        subTask.setDescription("This is a valid subtask description");
        t.addSubTask(subTask);

        TaskValidator validator = new TaskValidator(t);

        List<ServiceError> errors = validator.validateForCreation();
        System.out.println(errors.toString());

        Assert.assertTrue(errors.isEmpty());

    }

	@Test
	public void testDescriptionTooShort() {
		Task t = createValidTask();
        SubTask subTask = new SubTask();
        t.addSubTask(subTask);

        TaskValidator validator = new TaskValidator(t);

		List<ServiceError> errors = validator.validateForCreation();
		System.out.println(errors.toString());

		Assert.assertFalse(errors.isEmpty());
        Assert.assertTrue(errors.size()==1);

		Assert.assertTrue(errors.contains(errorDescriptionEmpty));

	}



}
