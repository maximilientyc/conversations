package com.tipi.conversations.domains.com.tipi.conversations.domains.repositories;

import com.tipi.conversations.domains.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maximilien on 30/12/2015.
 */
public class InMemoryUsersRepository implements CrudRepository<User, String> {

	private List<User> users = new ArrayList<>();

	@Override
	public User save(User user) {
		users.add(user);
		return user;
	}

	@Override
	public <S extends User> Iterable<S> save(Iterable<S> iterable) {
		return null;
	}

	@Override
	public User findOne(String userId) {
		return null;
	}

	@Override
	public boolean exists(String userId) {
		return false;
	}

	@Override
	public Iterable<User> findAll() {
		return users;
	}

	@Override
	public Iterable<User> findAll(Iterable<String> stringIterable) {
		return null;
	}

	@Override
	public long count() {
		return users.size();
	}

	@Override
	public void delete(String userId) {

	}

	@Override
	public void delete(User user) {
		users.remove(user);
	}

	@Override
	public void delete(Iterable<? extends User> userIterable) {

	}

	@Override
	public void deleteAll() {
		users.clear();
	}
}
