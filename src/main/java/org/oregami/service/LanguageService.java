package org.oregami.service;

import com.google.inject.Inject;
import org.oregami.entities.*;
import org.oregami.validation.LanguageValidator;

import java.util.List;

public class LanguageService {

	@Inject
	private LanguageDao languageDao = null;

    public ServiceResult<Language> createNewLanguage(Language languageData, ServiceCallContext context) {
        LanguageValidator validator = buildTaskValidator(languageData);

        List<ServiceError> errorMessages = validator.validateForCreation();

        Language language;

        if (errorMessages.size() == 0) {
            CustomRevisionListener.context.set(context);
            language = languageData;
            languageDao.save(language);
        }

        return new ServiceResult<>(languageData, errorMessages);
    }

    public ServiceResult<Language> updateLanguage(Language languageData, ServiceCallContext context) {
        LanguageValidator validator = buildTaskValidator(languageData);

        List<ServiceError> errorMessages = validator.validateForUpdate();

        Language language;

        if (errorMessages.size() == 0) {
            language = languageData;
            CustomRevisionListener.context.set(context);
            languageDao.update(language);
        }

        return new ServiceResult<>(languageData, errorMessages);
    }

	private LanguageValidator buildTaskValidator(Language newLanguage) {
		return new LanguageValidator(newLanguage);
	}
}
