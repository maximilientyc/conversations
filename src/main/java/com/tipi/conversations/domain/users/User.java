package com.tipi.conversations.domain.users;

import java.util.Arrays;

/**
 * Created by Maximilien on 28/01/2016.
 */
public class User {

	private final String userId;
	private String firstName;
	private String lastName;

	public User(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public User setFirstName(String firstName) {
		this.firstName = Arrays.asList(firstName.split("\\s+"))
				.stream()
				.map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
				.reduce((x, y) -> x + " " + y)
				.orElse("");
		return this;
	}

	public String firstName() {
		return firstName;
	}

	public User setLastName(String lastName) {
		this.lastName = Arrays.asList(lastName.split("\\s+"))
				.stream()
				.map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
				.reduce((x, y) -> x + " " + y)
				.orElse("");
		return this;
	}

	public String lastName() {
		return lastName;
	}
}
