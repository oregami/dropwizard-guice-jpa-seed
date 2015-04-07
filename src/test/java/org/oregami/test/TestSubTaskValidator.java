package org.oregami.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oregami.entities.SubTask;
import org.oregami.entities.Task;
import org.oregami.service.FieldNames;
import org.oregami.service.ServiceError;
import org.oregami.service.ServiceErrorContext;
import org.oregami.service.ServiceErrorMessage;
import org.oregami.util.StartHelper;
import org.oregami.validation.TaskValidator;

import java.util.List;

public class TestSubTaskValidator {

	ServiceError errorDescriptionEmpty = new ServiceError(new ServiceErrorContext(FieldNames.SUBTASK_DESCRIPTION), ServiceErrorMessage.SUBTASK_DESCRIPTION_EMPTY);
    ServiceError errorDescriptionTooShort = new ServiceError(new ServiceErrorContext(FieldNames.SUBTASK_DESCRIPTION), ServiceErrorMessage.SUBTASK_DESCRIPTION_TOO_SHORT);

    @BeforeClass
    public static void initClass() {
        StartHelper.init(StartHelper.CONFIG_FILENAME_TEST);
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
