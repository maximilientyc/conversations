package com.tipi.conversations.domains.users;

import com.tipi.conversations.domains.users.repositories.InMemoryUsersRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.repository.CrudRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Maximilien on 30/12/2015.
 */
public class UsersTest {

	private Users users;
	private CrudRepository usersRepository;

	@Before
	public void prepareUsers() {
		users = new Users(new InMemoryUsersRepository());
	}

	@Test
	public void should_contain_one_user() {
		// given
		users.add(new User("0001"));

		// when
		long userCount = users.getTotalUserCount();

		// then
		assertThat(userCount).isEqualTo(1);
	}

	@Test
	public void should_cotnain_two_users() {
		// given
		users.add(new User("0001"));
		users.add(new User("0002"));

		long userCount = users.getTotalUserCount();

		// then
		assertThat(userCount).isEqualTo(2);
	}

	@Test
	public void should_be_friends() {
		// given
		User firstUser = new User("0001");
		users.add(firstUser);
		User secondUser = new User("0002");
		users.add(secondUser);

		// when
		firstUser.addFriend(secondUser);

		// then
		firstUser = users.getByUserId("0001");
		secondUser = users.getByUserId("0002");
		assertThat(firstUser.isFriendWith(secondUser)).isTrue();
		assertThat(secondUser.isFriendWith(firstUser)).isTrue();
	}

	@Test
	public void should_not_be_friends() {
		// given
		User firstUser = new User("0001");
		users.add(firstUser);
		User secondUser = new User("0002");
		users.add(secondUser);

		// when

		// then
		firstUser = users.getByUserId("0001");
		secondUser = users.getByUserId("0002");
		assertThat(firstUser.isFriendWith(secondUser)).isFalse();
		assertThat(secondUser.isFriendWith(firstUser)).isFalse();
	}

}
