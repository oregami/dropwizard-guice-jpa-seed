package org.oregami.service;

import com.google.inject.Inject;
import org.oregami.entities.Language;
import org.oregami.entities.LanguageDao;
import org.oregami.entities.Task;
import org.oregami.entities.TaskDao;
import org.oregami.validation.LanguageValidator;
import org.oregami.validation.TaskValidator;

import java.util.List;

public class LanguageService {

	@Inject
	private LanguageDao languageDao;
	
    public ServiceResult<Language> createNewLanguage(Language languageData) {
        LanguageValidator validator = buildTaskValidator(languageData);

        List<ServiceError> errorMessages = validator.validateForCreation();

        Language language = null;

        if (errorMessages.size() == 0) {
            language = languageData;
            languageDao.save(language);
        }

        return new ServiceResult<Language>(languageData, errorMessages);
    }
    
    public ServiceResult<Language> updateLanguage(Language languageData) {
        LanguageValidator validator = buildTaskValidator(languageData);

        List<ServiceError> errorMessages = validator.validateForUpdate();

        Language language = null;

        if (errorMessages.size() == 0) {
            language = languageData;
            languageDao.update(language);
        }

        return new ServiceResult<Language>(languageData, errorMessages);
    }    

	private LanguageValidator buildTaskValidator(Language newLanguage) {
		return new LanguageValidator(this.languageDao, newLanguage);
	}
}
