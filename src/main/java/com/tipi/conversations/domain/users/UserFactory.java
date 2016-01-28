package com.tipi.conversations.domain.users;

/**
 * Created by Maximilien on 28/01/2016.
 */
public class UserFactory {

	private UserService userService;

	public UserFactory(UserService userService) {
		this.userService = userService;
	}

	public User buildUser() {
		String userId = userService.getNextUserId();
		return new User(userId);
	}
}
