package com.tipi.conversations.domain.conversations;

import com.tipi.conversations.domain.users.User;
import com.tipi.conversations.domain.users.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maximilien on 29/01/2016.
 */
public class UserRepositoryTest implements UserRepository {

	private List<User> users;


	public UserRepositoryTest() {
		users = new ArrayList<>();
		users.add(new User("max", "Maximilien", "tyc"));
		users.add(new User("bob", "Bob", "Marley"));
		users.add(new User("alice", "Alice", "Caglisse"));
	}

	@Override
	public User get(String userId) {
		for (User user : users) {
			if (user.userId().equals(userId)) {
				return user;
			}
		}
		return null;
	}
}