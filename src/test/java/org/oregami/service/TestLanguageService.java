package org.oregami.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.junit.*;
import org.oregami.dropwizard.ToDoApplication;
import org.oregami.entities.Language;
import org.oregami.entities.LanguageDao;
import org.oregami.test.PersistenceTest;

import javax.persistence.EntityManager;

/**
 * Created by sebastian on 25.08.14.
 */
public class TestLanguageService {

    static Injector injector = null;

    ServiceError errorNameTooShort = new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_NAME), ServiceErrorMessage.LANGUAGE_NAME_TOO_SHORT);
    ServiceError errorNameEmpty = new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_NAME), ServiceErrorMessage.LANGUAGE_NAME_EMPTY);
    ServiceError errorDescriptionEmpty = new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_DESCRIPTION), ServiceErrorMessage.LANGUAGE_DESCRIPTION_EMPTY);
    ServiceError errorDescriptionTooShort = new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_DESCRIPTION), ServiceErrorMessage.LANGUAGE_DESCRIPTION_TOO_SHORT);

    private EntityManager entityManager;

    @BeforeClass
    public static void init() {
        JpaPersistModule jpaPersistModule = new JpaPersistModule(ToDoApplication.JPA_UNIT);
        injector = Guice.createInjector(jpaPersistModule);
        injector.getInstance(PersistenceTest.class);
        PersistService persistService = injector.getInstance(PersistService.class);
        persistService.start();
    }

    @Before
    public void startTx() {
        if (entityManager==null) {
            entityManager = injector.getInstance(EntityManager.class);
        }
        entityManager.getTransaction().begin();

    }

    @After
    public void rollbackTx() {
        entityManager.getTransaction().rollback();
        errorDescriptionTooShort.getContext().setId(null);
    }

    @Test
    public void emptyDescriptionShouldGiveError() {
        LanguageService service = injector.getInstance(LanguageService.class);
        LanguageDao dao = injector.getInstance(LanguageDao.class);

        Language l = new Language("english");
        ServiceResult<Language> result = service.createNewLanguage(l);

        Assert.assertTrue(result.hasErrors());
        Assert.assertEquals(1, result.getErrors().size());
        Assert.assertTrue(result.containsError(errorDescriptionEmpty));
        Assert.assertEquals(0, dao.findAll().size());

    }

    @Test
    public void emptyNameShouldGiveError() {
        LanguageService service = injector.getInstance(LanguageService.class);
        LanguageDao dao = injector.getInstance(LanguageDao.class);

        Language l = new Language();
        l.setDescription("this is a description");
        ServiceResult<Language> result = service.createNewLanguage(l);

        Assert.assertTrue(result.hasErrors());
        Assert.assertEquals(1, result.getErrors().size());
        Assert.assertTrue(result.containsError(errorNameEmpty));
        Assert.assertEquals(0, dao.findAll().size());

        l.setName("a name");
        ServiceResult<Language> result2 = service.createNewLanguage(l);

        Assert.assertFalse(result2.hasErrors());

    }


    @Test
    public void saveLanguageWithoutErrorButWithErrorOnUpdate() {
        LanguageService service = injector.getInstance(LanguageService.class);
        LanguageDao dao = injector.getInstance(LanguageDao.class);

        Language l = new Language("name");
        l.setDescription("this is a description");
        ServiceResult<Language> result = service.createNewLanguage(l);

        Assert.assertFalse(result.hasErrors());
        Assert.assertEquals(0, result.getErrors().size());
        Assert.assertEquals(1, dao.findAll().size());

        l.setDescription("aa");
        ServiceResult<Language> updateResult = service.updateLanguage(l);
        Assert.assertTrue(updateResult.hasErrors());
        Assert.assertEquals(1, updateResult.getErrors().size());
        errorDescriptionTooShort.getContext().setId(l.getId());
        Assert.assertTrue(updateResult.containsError(errorDescriptionTooShort));
        Assert.assertEquals(1, dao.findAll().size());


    }


}
