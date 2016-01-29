package com.tipi.conversations.domain.conversations;

import com.tipi.conversations.domain.users.User;

/**
 * Created by Maximilien on 30/12/2015.
 */
public class Participant {

	private final User user;

	public Participant(User user) {
		this.user = user;
	}

}
