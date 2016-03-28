package com.tipi.conversations.domain;

/**
 * Created by @maximilientyc on 16/01/2016.
 */
public class ParticipantFactory {

	private final UserRepository userRepository;

	public ParticipantFactory(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Participant buildParticipant(User user) {
		return new Participant(user);
	}

	public Participant buildParticipant(String userId) {
		User user = userRepository.get(userId);
		return new Participant(user);
	}
}
