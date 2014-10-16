package org.oregami.validation;

import org.apache.commons.lang.StringUtils;
import org.oregami.entities.SubTask;
import org.oregami.entities.Task;
import org.oregami.entities.TaskDao;
import org.oregami.service.FieldNames;
import org.oregami.service.ServiceError;
import org.oregami.service.ServiceErrorContext;
import org.oregami.service.ServiceErrorMessage;

import java.util.ArrayList;
import java.util.List;

public class SubTaskValidator {

    private final SubTask subTask;

    private final int descriptionMinLenght = 3;

    public SubTaskValidator(SubTask subTask) {
        if (subTask == null) {
            throw new RuntimeException("org.oregami.taskvalidator.NoSubTaskGiven");
        }
        this.subTask = subTask;
    }

    public List<ServiceError> validateForCreation() {
        List<ServiceError> errors = new ArrayList<ServiceError>();

        errors.addAll(validateRequiredFields());

        return errors;

    }

    public List<ServiceError> validateRequiredFields() {
        List<ServiceError> errorMessages = new ArrayList<ServiceError>();

        String id = subTask.getId();
        if (id==null) {
            id = subTask.getErrorId();
        }
        if (StringUtils.isEmpty(subTask.getDescription())) {
            errorMessages.add(
                    new ServiceError(
                            new ServiceErrorContext(FieldNames.SUBTASK_DESCRIPTION, id),
                            ServiceErrorMessage.SUBTASK_DESCRIPTION_EMPTY
                    )
            );
        }
        else if (StringUtils.length(subTask.getDescription()) < descriptionMinLenght) {
        	errorMessages.add(
                    new ServiceError(
                            new ServiceErrorContext(FieldNames.SUBTASK_DESCRIPTION, id),
                            ServiceErrorMessage.SUBTASK_DESCRIPTION_TOO_SHORT
                    )
            );
        }

        return errorMessages;
    }

}
