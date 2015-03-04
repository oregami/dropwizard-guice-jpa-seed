package org.oregami.service;

import com.google.inject.Inject;
import org.oregami.entities.*;
import org.oregami.validation.LanguageValidator;
import org.oregami.validation.TaskValidator;

import java.util.List;

public class LanguageService {

	@Inject
	private LanguageDao languageDao;

    public ServiceResult<Language> createNewLanguage(Language languageData, ServiceCallContext context) {
        LanguageValidator validator = buildTaskValidator(languageData);

        List<ServiceError> errorMessages = validator.validateForCreation();

        Language language = null;

        if (errorMessages.size() == 0) {
            language = languageData;
            CustomRevisionListener.context.set(context);
            languageDao.save(language);
        }

        return new ServiceResult<Language>(languageData, errorMessages);
    }

    public ServiceResult<Language> updateLanguage(Language languageData, ServiceCallContext context) {
        LanguageValidator validator = buildTaskValidator(languageData);

        List<ServiceError> errorMessages = validator.validateForUpdate();

        Language language = null;

        if (errorMessages.size() == 0) {
            language = languageData;
            CustomRevisionListener.context.set(context);
            languageDao.update(language);
        }

        return new ServiceResult<Language>(languageData, errorMessages);
    }

	private LanguageValidator buildTaskValidator(Language newLanguage) {
		return new LanguageValidator(this.languageDao, newLanguage);
	}
}
