package com.tipi.conversations.domains.users;

import com.tipi.conversations.domains.users.repositories.InMemoryUsersRepository;
import com.tipi.conversations.infrastructure.sequences.Sequences;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.repository.CrudRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Maximilien on 30/12/2015.
 */
public class UsersTest {

	private Users users;
	private Sequences sequences;
	private CrudRepository usersRepository;

	@Before
	public void prepareUsers() {
		users = new Users(new InMemoryUsersRepository());
	}

	@Before
	public void prepareSequences() {
		sequences = new Sequences();
	}

	@Test
	public void should_contain_one_user() {
		// given
		users.add(new User(sequences.getNextUserId()));

		// when
		long userCount = users.getTotalUserCount();

		// then
		assertThat(userCount).isEqualTo(1);
	}

	@Test
	public void should_contain_two_users() {
		// given
		users.add(new User(sequences.getNextUserId()));
		users.add(new User(sequences.getNextUserId()));

		long userCount = users.getTotalUserCount();

		// then
		assertThat(userCount).isEqualTo(2);
	}

	@Test
	public void should_be_friends() {
		// given
		User firstUser = new User(sequences.getNextUserId());
		users.add(firstUser);
		User secondUser = new User(sequences.getNextUserId());
		users.add(secondUser);

		// when
		firstUser.addFriend(secondUser);

		// then
		firstUser = users.getByUserId(firstUser.getUserId());
		secondUser = users.getByUserId(secondUser.getUserId());
		assertThat(firstUser.isFriendWith(secondUser)).isTrue();
		assertThat(secondUser.isFriendWith(firstUser)).isTrue();
	}

	@Test
	public void should_not_be_friends() {
		// given
		User firstUser = new User(sequences.getNextUserId());
		users.add(firstUser);
		User secondUser = new User(sequences.getNextUserId());
		users.add(secondUser);

		// when

		// then
		firstUser = users.getByUserId(firstUser.getUserId());
		secondUser = users.getByUserId(secondUser.getUserId());
		assertThat(firstUser.isFriendWith(secondUser)).isFalse();
		assertThat(secondUser.isFriendWith(firstUser)).isFalse();
	}

}
