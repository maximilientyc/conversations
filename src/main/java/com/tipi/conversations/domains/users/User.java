package com.tipi.conversations.domains.users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maximilien on 30/12/2015.
 */
public class User {

	private final String userId;
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


}
