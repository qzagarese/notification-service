package com.example.notificationservice.db;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.example.notificationservice.core.Notification;
import com.example.notificationservice.core.User;
import com.google.common.base.Optional;

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
	
	public Optional<Notification> findOneIfUserMatches(String guid, User user) {
		return Optional.fromNullable((Notification)namedQuery(
				"com.example.notificationservice.core.Notification.findOneIfUserMatches")
				.setString("guid", guid).setEntity("user", user).uniqueResult());
	}

	public Map<String, List<Notification>> findSinceDateOrderByDateGroupByType(User user,
			Date date) {
		Map<String, List<Notification>> result = new HashMap<String, List<Notification>>();
		List<Notification> queryResult = list(namedQuery("com.example.notificationservice.core.Notification.findSinceDateOrderByTypeAndDate")
				.setEntity("user", user).setTimestamp("since", date));
		if (queryResult != null) {
			for (Notification notification : queryResult) {
				List<Notification> list = result.get(notification
						.getEventType());
				if (list == null) {
					list = new ArrayList<Notification>();
				}
				list.add(notification);
				result.put(notification.getEventType(), list);
			}
		}
		return result;
	}
	
	public List<Notification> findAll(){
		return list(namedQuery("com.example.notificationservice.core.Notification.findAll"));
	}
}
