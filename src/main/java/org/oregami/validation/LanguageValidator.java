package org.oregami.validation;

import org.apache.commons.lang.StringUtils;
import org.oregami.entities.Language;
import org.oregami.service.FieldNames;
import org.oregami.service.ServiceError;
import org.oregami.service.ServiceErrorContext;
import org.oregami.service.ServiceErrorMessage;

import java.util.ArrayList;
import java.util.List;

public class LanguageValidator {


    private final Language language;

    private final int nameMinLenght = 3;
    private final int descriptionMinLenght = 10;

    public LanguageValidator(Language l) {
        if (l == null) {
            throw new RuntimeException("org.oregami.taskvalidator.NoLanguageGiven");
        }

        this.language = l;
    }

    public List<ServiceError> validateForCreation() {
        List<ServiceError> errors = new ArrayList<ServiceError>();

        errors.addAll(validateRequiredFields());

        return errors;

    }

    public List<ServiceError> validateRequiredFields() {
        List<ServiceError> errorMessages = new ArrayList<ServiceError>();
        String id = language.getId();
        if (id==null) {
            id = language.getValidationId();
        }
        if (StringUtils.isEmpty(language.getName())) {
            errorMessages.add(new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_NAME, id), ServiceErrorMessage.LANGUAGE_NAME_EMPTY));
        }
        else if (StringUtils.length(language.getName()) < nameMinLenght) {
            errorMessages.add(new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_NAME, id), ServiceErrorMessage.LANGUAGE_NAME_TOO_SHORT));
        }


        if (StringUtils.isEmpty(language.getDescription())) {
            errorMessages.add(new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_DESCRIPTION, id), ServiceErrorMessage.LANGUAGE_DESCRIPTION_EMPTY));
        }
        else if (StringUtils.length(language.getDescription()) < descriptionMinLenght) {
            errorMessages.add(new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_DESCRIPTION, id), ServiceErrorMessage.LANGUAGE_DESCRIPTION_TOO_SHORT));
        }



        return errorMessages;
    }

	public List<ServiceError> validateForUpdate() {

        List<ServiceError> errors = new ArrayList<ServiceError>();

        errors.addAll(validateRequiredFields());

        return errors;
	}
}
