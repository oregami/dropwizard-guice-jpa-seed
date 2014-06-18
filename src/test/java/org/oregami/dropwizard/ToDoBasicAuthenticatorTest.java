package org.oregami.dropwizard;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentials;

import org.junit.Assert;
import org.junit.Test;
import org.oregami.user.User;

import com.google.common.base.Optional;

public class ToDoBasicAuthenticatorTest {

	
	@Test
	public void testValidPassword() {
		ToDoBasicAuthenticator authenticator = new ToDoBasicAuthenticator();
		BasicCredentials credentials = new BasicCredentials("user", "password");
		try {
			Optional<User> optional = authenticator.authenticate(credentials);
			Assert.assertNotNull(optional);
			Assert.assertNotNull(optional.get());
		} catch (AuthenticationException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testInvalidPassword() {
		ToDoBasicAuthenticator authenticator = new ToDoBasicAuthenticator();
		BasicCredentials credentials = new BasicCredentials("user", "wrongpassword");
		try {
			Optional<User> optional = authenticator.authenticate(credentials);
			Assert.assertNotNull(optional);
			Assert.assertEquals(optional, Optional.absent());
		} catch (AuthenticationException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Test
	public void testInvalidUsername() {
		ToDoBasicAuthenticator authenticator = new ToDoBasicAuthenticator();
		BasicCredentials credentials = new BasicCredentials("wronguser", "password");
		try {
			Optional<User> optional = authenticator.authenticate(credentials);
			Assert.assertNotNull(optional);
			Assert.assertEquals(optional, Optional.absent());
		} catch (AuthenticationException e) {
			Assert.fail(e.getMessage());
		}
	}	
	
}
