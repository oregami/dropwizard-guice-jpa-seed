package org.oregami.service;


import org.junit.Assert;
import org.junit.Test;

public class TestServiceErrorContext {
	
	@Test
	public void testHashCodeEqual() {
		ServiceErrorContext c1 = new ServiceErrorContext("test");
		ServiceErrorContext c2 = new ServiceErrorContext("test");
		
		Assert.assertTrue(c1.hashCode()==c2.hashCode());
	}

    @Test
    public void testHashCodeEqualId() {
        ServiceErrorContext c1 = new ServiceErrorContext("test", "id1");
        ServiceErrorContext c2 = new ServiceErrorContext("test", "id1");

        Assert.assertTrue(c1.hashCode()==c2.hashCode());
    }
	
	@Test
	public void testHashCodeNotEqual() {
		ServiceErrorContext c1 = new ServiceErrorContext("test");
		ServiceErrorContext c2 = new ServiceErrorContext("other");
		
		Assert.assertTrue(c1.hashCode()!=c2.hashCode());
	}

    @Test
    public void testHashCodeNotEqualId() {
        ServiceErrorContext c1 = new ServiceErrorContext("test", "id1");
        ServiceErrorContext c2 = new ServiceErrorContext("other", "id1");

        Assert.assertTrue(c1.hashCode()!=c2.hashCode());
    }

    @Test
    public void testHashCodeNotEqualId2() {
        ServiceErrorContext c1 = new ServiceErrorContext("test", "id1");
        ServiceErrorContext c2 = new ServiceErrorContext("test", "id2");

        Assert.assertTrue(c1.hashCode()!=c2.hashCode());
    }


    @Test
	public void testEquals() {
		ServiceErrorContext c1 = new ServiceErrorContext("test");
		ServiceErrorContext c2 = new ServiceErrorContext("test");
		
		Assert.assertTrue(c1.equals(c1));
		Assert.assertTrue(c1.equals(c2));
	}

    @Test
    public void testEqualsId() {
        ServiceErrorContext c1 = new ServiceErrorContext("test", "id1");
        ServiceErrorContext c2 = new ServiceErrorContext("test", "id1");

        Assert.assertTrue(c1.equals(c1));
        Assert.assertTrue(c1.equals(c2));
    }
	
	
	@Test
	public void testNotEquals() {
		ServiceErrorContext c1 = new ServiceErrorContext("test");
		ServiceErrorContext c2 = new ServiceErrorContext("other");
		
		Assert.assertFalse(c1.equals(c2));
	}

    @Test
    public void testNotEqualsId() {
        ServiceErrorContext c1 = new ServiceErrorContext("test", "id1");
        ServiceErrorContext c2 = new ServiceErrorContext("other", "id1");

        Assert.assertFalse(c1.equals(c2));
    }

    @Test
    public void testNotEqualsId2() {
        ServiceErrorContext c1 = new ServiceErrorContext("test", "id1");
        ServiceErrorContext c2 = new ServiceErrorContext("test", "id2");

        Assert.assertFalse(c1.equals(c2));
    }



}
