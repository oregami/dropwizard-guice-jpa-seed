package org.oregami.service;

import org.junit.*;
import org.oregami.entities.Language;
import org.oregami.entities.LanguageDao;
import org.oregami.util.StartHelper;

import javax.persistence.EntityManager;

/**
 * Created by sebastian on 25.08.14.
 */
public class TestLanguageService {

    ServiceError errorNameTooShort = new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_NAME), ServiceErrorMessage.LANGUAGE_NAME_TOO_SHORT);
    ServiceError errorNameEmpty = new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_NAME), ServiceErrorMessage.LANGUAGE_NAME_EMPTY);
    ServiceError errorDescriptionEmpty = new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_DESCRIPTION), ServiceErrorMessage.LANGUAGE_DESCRIPTION_EMPTY);
    ServiceError errorDescriptionTooShort = new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_DESCRIPTION), ServiceErrorMessage.LANGUAGE_DESCRIPTION_TOO_SHORT);

    private EntityManager entityManager;

    @BeforeClass
    public static void initClass() {
        StartHelper.init(StartHelper.CONFIG_FILENAME_TEST);
    }

    @Before
    public void startTx() {
        if (entityManager==null) {
            entityManager = StartHelper.getInstance(EntityManager.class);
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
        LanguageService service = StartHelper.getInstance(LanguageService.class);
        LanguageDao dao = StartHelper.getInstance(LanguageDao.class);

        Language l = new Language("english");
        ServiceResult<Language> result = service.createNewEntity(l, null);

        Assert.assertTrue(result.hasErrors());
        Assert.assertEquals(1, result.getErrors().size());
        Assert.assertTrue(result.containsError(errorDescriptionEmpty));
        Assert.assertEquals(0, dao.findAll().size());

    }

    @Test
    public void emptyNameShouldGiveError() {
        LanguageService service = StartHelper.getInstance(LanguageService.class);
        LanguageDao dao = StartHelper.getInstance(LanguageDao.class);

        Language l = new Language();
        l.setDescription("this is a description");
        ServiceResult<Language> result = service.createNewEntity(l, null);

        Assert.assertTrue(result.hasErrors());
        Assert.assertEquals(1, result.getErrors().size());
        Assert.assertTrue(result.containsError(errorNameEmpty));
        Assert.assertEquals(0, dao.findAll().size());

        l.setName("a name");
        ServiceResult<Language> result2 = service.createNewEntity(l, null);

        Assert.assertFalse(result2.hasErrors());

    }


    @Test
    public void saveLanguageWithoutErrorButWithErrorOnUpdate() {
        LanguageService service = StartHelper.getInstance(LanguageService.class);
        LanguageDao dao = StartHelper.getInstance(LanguageDao.class);

        Language l = new Language("name");
        l.setDescription("this is a description");
        ServiceResult<Language> result = service.createNewEntity(l, null);

        Assert.assertFalse(result.hasErrors());
        Assert.assertEquals(0, result.getErrors().size());
        Assert.assertEquals(1, dao.findAll().size());

        l.setDescription("aa");
        ServiceResult<Language> updateResult = service.updateEntity(l, null);
        Assert.assertTrue(updateResult.hasErrors());
        Assert.assertEquals(1, updateResult.getErrors().size());
        errorDescriptionTooShort.getContext().setId(l.getId());
        Assert.assertTrue(updateResult.containsError(errorDescriptionTooShort));
        Assert.assertEquals(1, dao.findAll().size());


    }


}
