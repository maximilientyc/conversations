package com.github.maximilientyc.conversations.domain;

/**
 * Created by @maximilientyc on 16/01/2016.
 */
public class ParticipantFactory {

	private final UserRepository userRepository;

	public ParticipantFactory(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Participant buildParticipant(String userId) {
		boolean userExists = userRepository.exists(userId);
		if (!userExists) {
			throw new IllegalArgumentException("Cannot create participant, reason: userId does not exist.");
		}
		User user = userRepository.get(userId);
		return new Participant(user);
	}
}
