package org.oregami.service;

import com.google.inject.Inject;
import org.oregami.entities.*;
import org.oregami.validation.IEntityValidator;
import org.oregami.validation.LanguageValidator;

public class LanguageService extends TopLevelEntityService<Language> {

    @Inject
    LanguageDao dao;

    @Override
    public GenericDAOUUID<Language, String> getDao() {
        return dao;
    }

    @Override
    public IEntityValidator buildEntityValidator(Language entity) {
        return new LanguageValidator(entity);
    }
}
