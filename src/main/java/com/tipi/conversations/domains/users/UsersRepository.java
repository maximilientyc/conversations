package com.tipi.conversations.domains.users;

/**
 * Created by Maximilien on 05/01/2016.
 */
public interface UsersRepository {

	User save(User user);

	User findOne(String userId);

	Iterable<User> findAll();

	long count();
}
