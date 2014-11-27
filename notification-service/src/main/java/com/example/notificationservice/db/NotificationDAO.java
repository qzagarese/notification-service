package com.example.notificationservice.db;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import com.example.notificationservice.core.Notification;
import com.example.notificationservice.core.User;

public class NotificationDAO extends AbstractDAO<Notification> {

	public NotificationDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Notification create(Notification notification) {
		return persist(notification);
	}

	public void update(Notification notification) {
		persist(notification);
	}

	
	public void delete(Notification notification) {
		currentSession().delete(notification);
	}
	
	public Notification findOneIfUserMatches(String guid, User user) {
		return (Notification) namedQuery(
				"com.example.notificationservice.core.Notification.findOneIfUserMatches")
				.setString("guid", guid).setEntity("user", user).uniqueResult();
	}

	public List<Notification> findSinceDateOrderByTypeAndDate(User user,
			Date date) {
		return list(namedQuery("com.example.notificationservice.core.Notification.findSinceDateOrderByTypeAndDate")
				.setEntity("user", user).setTimestamp("since", date));
	}
	
	public List<Notification> findAll(){
		return list(namedQuery("com.example.notificationservice.core.Notification.findAll"));
	}
}
