package com.example.notificationservice.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.codahale.metrics.annotation.Timed;
import com.example.notificationservice.core.Notification;
import com.example.notificationservice.core.User;
import com.example.notificationservice.db.NotificationDAO;
import com.google.common.base.Optional;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {

	private final NotificationDAO dao;

	public NotificationResource(NotificationDAO dao) {
		this.dao = dao;
	}

	@Path("/{guid}")
	@PATCH
	@Timed
	@UnitOfWork
	public Response read(@Auth User user, @PathParam("guid") String guid) {
		Optional<Notification> notification = fetchAndSetRead(guid, user, true);
		return (notification.isPresent()) ? Response.ok(notification).build()
				: Response.status(Status.NOT_FOUND).build();
	}

	@Path("/latest")
	@GET
	@Timed
	@UnitOfWork
	public Map<String, List<Notification>> list(@Auth User user,
			@QueryParam("since") Optional<Long> since) {
		Map<String, List<Notification>> searchResult = dao
				.findSinceDateOrderByDateGroupByType(
						user,
						new Date(
								since.or(System.currentTimeMillis() - 3600000 * 24)));
		return searchResult;
	}

	@Path("/{guid}/read")
	@PATCH
	@Timed
	@UnitOfWork
	public Response markRead(@Auth User user, @PathParam("guid") String guid) {
		Optional<Notification> notification = fetchAndSetRead(guid, user, true);
		return (notification.isPresent()) ? Response.ok().build() : Response
				.status(Status.NOT_FOUND).build();
	}

	@Path("/{guid}/unread")
	@PATCH
	@Timed
	@UnitOfWork
	public Response markUnread(@Auth User user, @PathParam("guid") String guid) {
		Optional<Notification> notification = fetchAndSetRead(guid, user, false);
		return (notification.isPresent()) ? Response.ok().build() : Response
				.status(Status.NOT_FOUND).build();
	}

	@Path("/{guid}")
	@DELETE
	@Timed
	@UnitOfWork
	public Response delete(@Auth User user, @PathParam("guid") String guid) {
		Optional<Notification> notification = dao.findOneIfUserMatches(guid,
				user);
		if (notification.isPresent()) {
			dao.delete(notification.get());
			return Response.status(204).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	private Optional<Notification> fetchAndSetRead(String guid, User user,
			boolean read) {
		Optional<Notification> notification = dao.findOneIfUserMatches(guid,
				user);
		// If the notification does not exist or does not belong to the user,
		// raise a 404. We don't tell malicious users the object actually
		// exists
		if (notification.isPresent()) {
			notification.get().setRead(read);
			dao.update(notification.get());
		}
		return notification;
	}
}
