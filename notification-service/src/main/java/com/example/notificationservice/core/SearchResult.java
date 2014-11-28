package com.example.notificationservice.core;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SearchResult<T> {

	private Map<String, CategoryResult<T>> content;

	private long items = 0;

	private long total = 0;

	private long unread = 0;

	public long getItems() {
		return items;
	}

	public void setItems(long items) {
		this.items = items;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getUnread() {
		return unread;
	}

	public void setUnread(long unread) {
		this.unread = unread;
	}

	public Map<String, CategoryResult<T>> getContent() {
		return content;
	}

	public void setContent(Map<String, CategoryResult<T>> content) {
		this.content = content;
	}

}
