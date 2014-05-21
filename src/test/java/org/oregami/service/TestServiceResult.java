package org.oregami.service;

import org.junit.Assert;
import org.junit.Test;
import org.oregami.entities.BaseEntityUUID;

public class TestServiceResult {
	
	@Test
	public void testContainsErrorMessage() {
		ServiceResult<BaseEntityUUID> result = new ServiceResult<BaseEntityUUID>();
		result.addMessage(new ServiceErrorContext(FieldNames.TASK_NAME),ServiceErrorMessage.TASK_TASKNAME_EMPTY);
		
		Assert.assertTrue(result.containsError(new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_EMPTY)));
		Assert.assertFalse(result.containsError(new ServiceError(new ServiceErrorContext(FieldNames.TASK_DESCRIPTION), ServiceErrorMessage.TASK_DESCRIPTION_EMPTY)));
	}

	
	@Test
	public void testContainsErrorMessage2() {
		ServiceResult<BaseEntityUUID> result = new ServiceResult<BaseEntityUUID>();
		result.addMessage(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_EMPTY);
		result.addMessage(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_TOO_SHORT);
		result.addMessage(new ServiceErrorContext(FieldNames.TASK_DESCRIPTION), ServiceErrorMessage.TASK_DESCRIPTION_EMPTY);
		
		Assert.assertTrue(result.containsError(new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_EMPTY)));
		Assert.assertTrue(result.containsError(new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_TOO_SHORT)));
		Assert.assertTrue(result.containsError(new ServiceError(new ServiceErrorContext(FieldNames.TASK_DESCRIPTION), ServiceErrorMessage.TASK_DESCRIPTION_EMPTY)));
		Assert.assertFalse(result.containsError(new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_DESCRIPTION_EMPTY)));
		
		
	}
	
}
