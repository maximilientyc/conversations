package com.tipi.conversations.domain.users;

import java.util.UUID;

/**
 * Created by Maximilien on 28/01/2016.
 */
public class UserService {

	public String getNextUserId() {
		return UUID.randomUUID().toString();
	}
}
