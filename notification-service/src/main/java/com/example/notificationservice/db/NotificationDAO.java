package com.example.notificationservice.db;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.example.notificationservice.core.CategoryResult;
import com.example.notificationservice.core.Notification;
import com.example.notificationservice.core.SearchResult;
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
		return Optional
				.fromNullable((Notification) namedQuery(
						"com.example.notificationservice.core.Notification.findOneIfUserMatches")
						.setString("guid", guid).setEntity("user", user)
						.uniqueResult());
	}

	public SearchResult<Notification> findSinceDateOrderByDateGroupByType(
			User user, Date since, Date until) {
		SearchResult<Notification> result = new SearchResult<Notification>();
		
		// Retrieve data from DB
		List<Notification> queryResult = list(namedQuery(
				"com.example.notificationservice.core.Notification.findByDateOrderByDate")
				.setEntity("user", user).setTimestamp("since", since)
				.setTimestamp("until", until));
		@SuppressWarnings("unchecked")
		List<Object[]> totalNotifications = namedQuery(
				"com.example.notificationservice.core.Notification.countByUserGroupByEventType")
				.setEntity("user", user).list();
		@SuppressWarnings("unchecked")
		List<Object[]> unreadNotifications = namedQuery(
				"com.example.notificationservice.core.Notification.countUnreadByUserGroupByEventType")
				.setEntity("user", user).list();

		
		// Create result
		Map<String, CategoryResult<Notification>> content = new HashMap<String, CategoryResult<Notification>>();
		long items = 0;
		long total = 0;
		long unread = 0;
		if (queryResult != null) {
			for (Notification notification : queryResult) {
				CategoryResult<Notification> categoryResult = content.get(notification
						.getEventType());
				if (categoryResult == null) {
					categoryResult = new CategoryResult<Notification>();
					categoryResult.setContent(new ArrayList<Notification>());
				}
				categoryResult.getContent().add(notification);
				categoryResult.setItems(categoryResult.getItems() + 1);
				items++;
				content.put(notification.getEventType(), categoryResult);
			}
		}

		// Update result with number of notifications by eventType
		if(totalNotifications != null){
			for (Object[] objects : totalNotifications) {
				CategoryResult<Notification> categoryResult = content.get(objects[0]);
				categoryResult.setTotal(categoryResult.getTotal() + Long.valueOf((objects[1].toString())));
				total += Long.valueOf(objects[1].toString());
			}
		}

		// Update result with number of unread notifications by eventType
		if(unreadNotifications != null){
			for (Object[] objects : unreadNotifications) {
				CategoryResult<Notification> categoryResult = content.get(objects[0]);
				categoryResult.setUnread(categoryResult.getUnread() + Long.valueOf((objects[1].toString())));
				unread += Long.valueOf(objects[1].toString());
			}
		}
		
		result.setContent(content);
		result.setItems(items);
		result.setTotal(total);
		result.setUnread(unread);
		return result;
	}

	public List<Notification> findAll() {
		return list(namedQuery("com.example.notificationservice.core.Notification.findAll"));
	}
}
