package com.github.maximilientyc.conversations.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by @maximilientyc on 28/03/2016.
 */
public class UserTest {

	@Rule
	public final ExpectedException expectedException;
	private final UserRepository userRepository;
	private final ParticipantFactory participantFactory;

	public UserTest() {
		userRepository = new SampleUserRepository();
		participantFactory = new ParticipantFactory(userRepository);
		expectedException = ExpectedException.none();
	}

	@Test
	public void should_return_an_error_building_a_participant_with_a_non_existing_user() {
		// given
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot create participant, reason: userId 'john_doe' does not exist.");

		// when
		Participant maximilien = participantFactory.buildParticipant("john_doe");

	}
}
