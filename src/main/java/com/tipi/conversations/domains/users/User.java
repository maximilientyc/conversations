package com.tipi.conversations.domains.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Maximilien on 30/12/2015.
 */
public class User {

	private String userId;
	private List<User> friends = new ArrayList<>();

	public User(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void addFriend(User newFriend) {
		friends.add(newFriend);
		if (!newFriend.isFriendWith(this)) {
			newFriend.addFriend(this);
		}
	}

	public boolean isFriendWith(User secondUser) {
		return friends.contains(secondUser);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(userId, user.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}

}
