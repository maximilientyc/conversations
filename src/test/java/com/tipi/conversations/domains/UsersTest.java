package com.tipi.conversations.domains;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Maximilien on 30/12/2015.
 */
public class UsersTest {

	private CrudRepository usersRepository;

	@Before
	public void prepareUsersRepository() {
		usersRepository = new InMemoryUsersRepository();
	}

	@Test
	public void should_return_one_user() {
		// given
		usersRepository.save(new User("0001"));

		// when
		long userCount = usersRepository.count();

		// then
		assertThat(userCount).isEqualTo(1);
	}

	@Test
	public void should_return_two_users() {
		// given
		usersRepository.save(new User("0001"));
		usersRepository.save(new User("0002"));

		long userCount = usersRepository.count();

		// then
		assertThat(userCount).isEqualTo(2);
	}

	@Test
	public void should_be_friends() {
		// given
		User firstUser = new User("0001");
		User secondUser = new User("0002");

		// when
		firstUser.addFriend(secondUser);

		// then
		assertThat(firstUser.isFriendWith(secondUser)).isTrue();
		assertThat(secondUser.isFriendWith(firstUser)).isTrue();
	}

	@Test
	public void should_not_be_friends() {
		// given
		User firstUser = new User("0001");
		User secondUser = new User("0002");

		// when

		// then
		assertThat(firstUser.isFriendWith(secondUser)).isFalse();
		assertThat(secondUser.isFriendWith(firstUser)).isFalse();
	}


}
