package com.example.notificationservice;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.example.notificationservice.auth.SimpleAuthenticator;
import com.example.notificationservice.core.Notification;
import com.example.notificationservice.core.User;
import com.example.notificationservice.db.NotificationDAO;
import com.example.notificationservice.db.UserDAO;
import com.example.notificationservice.resources.NotificationResource;

public class NotificationServiceApplication extends
		Application<NotificationServiceConfiguration> {
	public static void main(String[] args) throws Exception {
		new NotificationServiceApplication().run(args);
	}

	private final HibernateBundle<NotificationServiceConfiguration> hibernateBundle = new HibernateBundle<NotificationServiceConfiguration>(
			Notification.class, User.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(
				NotificationServiceConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	@Override
	public String getName() {
		return "notification-service";
	}

	@Override
	public void initialize(Bootstrap<NotificationServiceConfiguration> bootstrap) {
		bootstrap
				.addBundle(new MigrationsBundle<NotificationServiceConfiguration>() {
					@Override
					public DataSourceFactory getDataSourceFactory(
							NotificationServiceConfiguration configuration) {
						return configuration.getDataSourceFactory();
					}
				});
		bootstrap.addBundle(hibernateBundle);
	}

	@Override
	public void run(NotificationServiceConfiguration configuration,
			Environment environment) {
		final NotificationDAO notificationDao = new NotificationDAO(
				hibernateBundle.getSessionFactory());
		final UserDAO userDAO = new UserDAO(hibernateBundle.getSessionFactory());
		environment.jersey().register(
				new BasicAuthProvider<User>(
						new SimpleAuthenticator<BasicCredentials, User>(userDAO),
						"Secured Ops"));

		environment.jersey()
				.register(new NotificationResource(notificationDao));
	}
}
