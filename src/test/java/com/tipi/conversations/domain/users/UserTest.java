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
		String userId = user.getUserId();

		// then
		assertThat(userId).isNotNull();
	}

	@Test
	public void should_have_a_capitalized_first_name() {
		// given
		User user = userFactory.buildUser().setFirstName("mAximILien   juNior m");

		// when
		String firstName = user.firstName();

		// then
		assertThat(firstName).isEqualTo("Maximilien Junior M");
	}

	@Test
	public void should_have_a_capitalized_last_name() {
		// given
		User user = userFactory.buildUser()
				.setFirstName("mAximILien   juNior m")
				.setLastName("dE LA vEGA");

		// when
		String lastName = user.lastName();

		// then
		assertThat(lastName).isEqualTo("De La Vega");
	}
}
