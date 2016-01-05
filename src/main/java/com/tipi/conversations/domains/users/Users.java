package com.tipi.conversations.domains.users;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Maximilien on 02/01/2016.
 */
public class Users {

	private UsersRepository usersRepository;

	public Users(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	public User add(User user) {
		return usersRepository.save(user);
	}

	public User getByUserId(String userId) {
		return usersRepository.findOne(userId);
	}

	public long getTotalUserCount() {
		return usersRepository.count();
	}
}
