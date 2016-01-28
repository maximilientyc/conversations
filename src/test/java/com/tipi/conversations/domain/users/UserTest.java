package com.tipi.conversations.domain.users;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Maximilien on 28/01/2016.
 */
public class UserTest {

	private UserService userService;
	private UserFactory userFactory;

	public UserTest() {
		this.userService = new UserService();
		this.userFactory = new UserFactory(userService);
	}

	@Test
	public void should_have_an_id_when_new() {
		// given
		User user = userFactory.buildUser();

		// when

		// then
		assertThat(user.userId()).isNotNull();
	}

	@Test
	public void should_have_a_capitalized_first_name() {
		// given
		User user = userFactory.buildUser();

		// when
		user.setFirstName("mAximILien   juNior m");

		// then
		assertThat(user.firstName()).isEqualTo("Maximilien Junior M");
	}

	@Test
	public void should_have_a_capitalized_last_name() {
		// given
		User user = userFactory.buildUser()
				.setFirstName("mAximILien   juNior m");

		// when
		user.setLastName("dE LA vEGA");

		// then
		assertThat(user.lastName()).isEqualTo("De La Vega");
	}
}
