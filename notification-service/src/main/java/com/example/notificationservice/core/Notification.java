package com.example.notificationservice.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "notifications")
@NamedQueries({
		@NamedQuery(name = "com.example.notificationservice.core.Notification.findOneIfUserMatches", query = "SELECT n FROM Notification n WHERE n.guid = :guid AND n.user = :user"),
		@NamedQuery(name = "com.example.notificationservice.core.Notification.findSinceDateOrderByTypeAndDate", query = "SELECT n "
				+ "FROM Notification n "
				+ "WHERE n.user = :user AND n.eventTimestamp > :since "
				+ "ORDER BY n.eventType, n.eventTimestamp DESC"),
		@NamedQuery(name = "com.example.notificationservice.core.Notification.findAll", query = "SELECT n FROM Notification n ") })
@JsonInclude(Include.NON_NULL)
public class Notification {

	@Id
	private String guid;

	@Column(nullable = false, updatable = false)
	private String deviceGuid;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user", referencedColumnName = "guid")
	private User user;

	@Column(nullable = false, updatable = false)
	private String eventType;

	@Column(updatable = false)
	private String eventSubtype;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = true)
	private Double geofenceLat;

	@Column(nullable = true)
	private Double geofenceLon;

	@Column(nullable = true)
	private Long geofenceRadiusMetres;

	@Temporal(TemporalType.TIMESTAMP)
	private Date eventTimestamp;

	@Temporal(TemporalType.TIMESTAMP)
	private Date sentTimestamp;

	@Column
	private Boolean read = false;

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getGeofenceLat() {
		return geofenceLat;
	}

	public void setGeofenceLat(Double geofenceLat) {
		this.geofenceLat = geofenceLat;
	}

	public Double getGeofenceLon() {
		return geofenceLon;
	}

	public void setGeofenceLon(Double geofenceLon) {
		this.geofenceLon = geofenceLon;
	}

	public Long getGeofenceRadiusMetres() {
		return geofenceRadiusMetres;
	}

	public void setGeofenceRadiusMetres(Long geofenceRadiusMetres) {
		this.geofenceRadiusMetres = geofenceRadiusMetres;
	}

	public Date getEventTimestamp() {
		return eventTimestamp;
	}

	public void setEventTimestamp(Date eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	public Date getSentTimestamp() {
		return sentTimestamp;
	}

	public void setSentTimestamp(Date sentTimestamp) {
		this.sentTimestamp = sentTimestamp;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getDeviceGuid() {
		return deviceGuid;
	}

	public void setDeviceGuid(String deviceGuid) {
		this.deviceGuid = deviceGuid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventSubtype() {
		return eventSubtype;
	}

	public void setEventSubtype(String eventSubtype) {
		this.eventSubtype = eventSubtype;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((guid == null) ? 0 : guid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Notification other = (Notification) obj;
		if (guid == null) {
			if (other.guid != null)
				return false;
		} else if (!guid.equals(other.guid))
			return false;
		return true;
	}

}
