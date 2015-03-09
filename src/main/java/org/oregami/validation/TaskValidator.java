package org.oregami.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.oregami.entities.SubTask;
import org.oregami.entities.Task;
import org.oregami.entities.TaskDao;
import org.oregami.service.FieldNames;
import org.oregami.service.ServiceError;
import org.oregami.service.ServiceErrorContext;
import org.oregami.service.ServiceErrorMessage;

public class TaskValidator {

    private final Task task;

    private final int nameMinLenght = 3;

    public TaskValidator(Task task) {
        if (task == null) {
            throw new RuntimeException("org.oregami.taskvalidator.NoTaskGiven");
        }

        this.task = task;
    }

    public List<ServiceError> validateForCreation() {
        List<ServiceError> errors = new ArrayList<ServiceError>();

        errors.addAll(validateRequiredFields());

        errors.addAll(validateSubTasks());

        return errors;

    }

    private List<ServiceError> validateSubTasks() {
        List<ServiceError> errorMessages = new ArrayList<ServiceError>();
        Set<SubTask> subTasks = task.getSubTasks();
        for (SubTask subTask: subTasks ) {
            SubTaskValidator subTaskValidator = new SubTaskValidator(subTask);
            errorMessages.addAll(subTaskValidator.validateForCreation());
        }
        return errorMessages;
    }


    public List<ServiceError> validateRequiredFields() {
        List<ServiceError> errorMessages = new ArrayList<ServiceError>();
        String id = task.getId();
        if (id==null) {
            id = task.getValidationId();
        }
        if (StringUtils.isEmpty(task.getName())) {
            errorMessages.add(new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME, id), ServiceErrorMessage.TASK_TASKNAME_EMPTY));
        }
        else if (StringUtils.length(task.getName()) < nameMinLenght) {
        	errorMessages.add(new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME, id), ServiceErrorMessage.TASK_TASKNAME_TOO_SHORT));
        }

        if (StringUtils.isEmpty(task.getDescription())) {
            errorMessages.add(new ServiceError(new ServiceErrorContext(FieldNames.TASK_DESCRIPTION, id), ServiceErrorMessage.TASK_DESCRIPTION_EMPTY));
        }



        return errorMessages;
    }

	public List<ServiceError> validateForUpdate() {

        List<ServiceError> errors = new ArrayList<ServiceError>();

        errors.addAll(validateRequiredFields());

        errors.addAll(validateSubTasks());

        return errors;
	}
}
