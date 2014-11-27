package com.example.notificationservice.db;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import org.hibernate.SessionFactory;

import com.example.notificationservice.core.User;

public class UserDAO extends AbstractDAO<User> {

	
	public UserDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public User findByUsername(String username){
		User result = (User) namedQuery(
				"com.example.notificationservice.core.User.findByUsername")
				.setString("username", username).uniqueResult();
		return result;
	}

	public List<User> findAll(){
		return list(namedQuery("com.example.notificationservice.core.User.findAll"));	
	}
}
