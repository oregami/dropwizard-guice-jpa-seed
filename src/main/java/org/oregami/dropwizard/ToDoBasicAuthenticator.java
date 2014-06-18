package org.oregami.dropwizard;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import org.oregami.user.User;

import com.google.common.base.Optional;

public class ToDoBasicAuthenticator implements Authenticator<BasicCredentials, User> {
	
    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
    	
        if (isPasswordValid(credentials.getUsername(), credentials.getPassword())) {
        	User u = new User();
            return Optional.of(u);
        }
        return Optional.absent();
    }
	
	private boolean isPasswordValid(String username, String password) {
		boolean result = false;
		if ("user".equals(username) && "password".equals(password)) {
			result = true;
		}
		return result;
	}
}
