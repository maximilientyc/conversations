package com.tipi.conversations.domain.users;

/**
 * Created by Maximilien on 28/01/2016.
 */
public class User {

	private final String userId;
	private final String firstName;
	private final String lastName;

	public User(String userId, String firstName, String lastName) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String userId() {
		return userId;
	}

	public String firstName() {
		return firstName;
	}

	public String lastName() {
		return lastName;
	}

}
