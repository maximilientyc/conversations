package com.tipi.conversations.domain.users;

/**
 * Created by Maximilien on 29/01/2016.
 */
public interface UserRepository {

	/**
	 * Method used for clients to get a user. You need to implement this repository interface
	 * in order to link conversation @Participant with your user management implementation.
	 *
	 * @param userId
	 * @return user referenced by @userId
	 * @see com.tipi.conversations.domain.conversations.Participant
	 */
	User get(String userId);

}
