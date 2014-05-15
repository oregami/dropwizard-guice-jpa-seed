package org.oregami.service;


import org.junit.Assert;
import org.junit.Test;

public class TestServiceError {
	
	@Test
	public void testHashCodeEqual() {
		ServiceError c1 = new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_EMPTY);
		ServiceError c2 = new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_EMPTY);
		
		Assert.assertTrue(c1.hashCode()==c2.hashCode());
		
	}
	
	@Test
	public void testHashCodeNotEqual() {
		ServiceError c1 = new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_EMPTY);
		ServiceError c2 = new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_TOO_SHORT);
		ServiceError c3 = new ServiceError(new ServiceErrorContext(FieldNames.TASK_FINISHED), ServiceErrorMessage.TASK_FINISHED_EMPTY);
		
		Assert.assertFalse(c1.hashCode()==c2.hashCode());
		Assert.assertFalse(c1.hashCode()==c3.hashCode());
		Assert.assertFalse(c2.hashCode()==c3.hashCode());
		
	}
	
	@Test
	public void testEquals() {
		ServiceError c1 = new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_EMPTY);
		ServiceError c2 = new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_EMPTY);
		
		Assert.assertEquals(c1, c1);
		Assert.assertEquals(c1, c2);
		
	}	
	
	@Test
	public void testNotEquals() {
		ServiceError c1 = new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_EMPTY);
		ServiceError c2 = new ServiceError(new ServiceErrorContext(FieldNames.TASK_NAME), ServiceErrorMessage.TASK_TASKNAME_TOO_SHORT);
		ServiceError c3 = new ServiceError(new ServiceErrorContext(FieldNames.TASK_FINISHED), ServiceErrorMessage.TASK_FINISHED_EMPTY);
		
		Assert.assertNotEquals(c1, c2);
		Assert.assertNotEquals(c1, c3);
		Assert.assertNotEquals(c2, c3);
		
	}	
	
	

	
}
