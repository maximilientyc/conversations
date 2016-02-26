package com.tipi.conversations.domain.conversations;

import com.tipi.conversations.domain.users.User;

import java.util.Date;
import java.util.Objects;

/**
 * Created by @maximilientyc on 30/12/2015.
 */
public class Participant {

	private final User user;
	private final Date createdOn;

	public Participant(User user) {
		this.user = user;
		this.createdOn = new Date();
	}

	public Participant(User user, Date createdOn) {
		this.user = user;
		this.createdOn = createdOn;
	}

	public User getUser() {
		return user;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Participant that = (Participant) o;
		return Objects.equals(user, that.user) &&
				Objects.equals(createdOn, that.createdOn);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, createdOn);
	}
}
