package com.tipi.conversations.domain.conversations;

import com.tipi.conversations.domain.users.User;

import java.util.Objects;

/**
 * Created by @maximilientyc on 30/12/2015.
 */
public class Participant {

	private final User user;

	public Participant(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Participant that = (Participant) o;
		return Objects.equals(user, that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user);
	}
}
