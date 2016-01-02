package com.tipi.conversations.domains;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Maximilien on 02/01/2016.
 */
public class Users {

	private CrudRepository<User, String> usersRepository;

	public Users(CrudRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	public User addUser(User user) {
		return usersRepository.save(user);
	}

	public long getTotalUserCount() {
		return usersRepository.count();
	}
}
