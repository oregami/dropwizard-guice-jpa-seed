package org.oregami.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oregami.entities.Language;
import org.oregami.service.FieldNames;
import org.oregami.service.ServiceError;
import org.oregami.service.ServiceErrorContext;
import org.oregami.service.ServiceErrorMessage;
import org.oregami.util.StartHelper;
import org.oregami.validation.LanguageValidator;

import java.util.List;

public class TestLanguageValidator {

	ServiceError errorNameTooShort = new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_NAME), ServiceErrorMessage.LANGUAGE_NAME_TOO_SHORT);
	ServiceError errorNameEmpty = new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_NAME), ServiceErrorMessage.LANGUAGE_NAME_EMPTY);
	ServiceError errorDescriptionEmpty = new ServiceError(new ServiceErrorContext(FieldNames.LANGUAGE_DESCRIPTION), ServiceErrorMessage.LANGUAGE_DESCRIPTION_EMPTY);

    @BeforeClass
    public static void initClass() {
        StartHelper.init(StartHelper.CONFIG_FILENAME_TEST);
    }

	@Test
	public void testNameEmpty() {
		Language l = new Language();
		l.setDescription("This is a description");

		LanguageValidator validator = new LanguageValidator(l);

		List<ServiceError> errors = validator.validateForCreation();
		System.out.println(errors.toString());

		Assert.assertFalse(errors.isEmpty());
		Assert.assertEquals(1, errors.size());

		Assert.assertTrue(errors.contains(errorNameEmpty));
		Assert.assertFalse(errors.contains(errorNameTooShort));
		Assert.assertFalse(errors.contains(errorDescriptionEmpty));

	}

	@Test
	public void testNoErrors() {
        Language l = new Language("name");
		l.setDescription("This is a description");

        LanguageValidator validator = new LanguageValidator(l);

		List<ServiceError> errors = validator.validateForCreation();
		System.out.println(errors.toString());

		Assert.assertTrue(errors.isEmpty());
		Assert.assertEquals(0, errors.size());

	}

	@Test
	public void testNameTooShort() {
        Language l = new Language("aa");
		l.setDescription("This is a description");

        LanguageValidator validator = new LanguageValidator(l);

		List<ServiceError> errors = validator.validateForCreation();
		System.out.println(errors.toString());

		Assert.assertFalse(errors.isEmpty());
		Assert.assertEquals(1, errors.size());

		Assert.assertFalse(errors.contains(errorNameEmpty));
		Assert.assertFalse(errors.contains(errorDescriptionEmpty));
		Assert.assertTrue(errors.contains(errorNameTooShort));

	}

	@Test
	public void testDescriptionTooShort() {
        Language l = new Language("task name");

        LanguageValidator validator = new LanguageValidator(l);

		List<ServiceError> errors = validator.validateForCreation();
		System.out.println(errors.toString());

		Assert.assertFalse(errors.isEmpty());
		Assert.assertEquals(1, errors.size());

		Assert.assertFalse(errors.contains(errorNameEmpty));
		Assert.assertFalse(errors.contains(errorNameTooShort));
		Assert.assertTrue(errors.contains(errorDescriptionEmpty));

	}



}
