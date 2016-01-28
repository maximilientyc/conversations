package com.tipi.conversations.domain.users;

import java.util.UUID;

/**
 * Created by Maximilien on 28/01/2016.
 */
public class UserServiceTest extends UserService {

	@Override
	public String getNextUserId() {

		// plug here some specific user id retrieval

		return UUID.randomUUID().toString();
	}

}
