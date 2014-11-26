package com.example.notificationservice.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import com.example.notificationservice.core.User;
import com.example.notificationservice.db.UserDAO;
import com.google.common.base.Optional;

public class SimpleAuthenticator<T1, T2> implements
		Authenticator<BasicCredentials, User> {

	final UserDAO dao;

	public SimpleAuthenticator(UserDAO userDAO) {
		dao = userDAO;
	}

	@Override
	public Optional<User> authenticate(BasicCredentials credentials)
			throws AuthenticationException {
		Optional<User> result = Optional.fromNullable(dao
				.findByUsername(credentials.getUsername()));
		if (result.isPresent()
				&& !result.get().getPass().equals(credentials.getPassword())) {
			result = Optional.absent();
		}
		return result;
	}

}
