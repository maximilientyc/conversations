package com.tipi.conversations.domains.users.repositories;

import com.tipi.conversations.domains.users.User;
import com.tipi.conversations.domains.users.UsersRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Maximilien on 30/12/2015.
 */
public class InMemoryUsersRepository implements UsersRepository {

	private List<User> users = new ArrayList<>();

	@Override
	public User save(User user) {
		users.add(user);
		return user;
	}

	@Override
	public User findOne(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId parameter should not be null.");
		}
		Iterator<User> iterator = findAll().iterator();
		while (iterator.hasNext()) {
			User currentUser = iterator.next();
			if (currentUser.getUserId().equals(userId)) {
				return currentUser;
			}
		}
		return null;
	}

	@Override
	public Iterable<User> findAll() {
		return users;
	}

	@Override
	public long count() {
		return users.size();
	}

}
