package com.github.maximilientyc.conversations.domain;

/**
 * Created by @maximilientyc on 29/01/2016.
 */
public interface UserRepository {

	/**
	 * Method used to get a user. This method needs to be implemented by clients
	 * in order to link conversation @Participant with client specific user management implementation.
	 *
	 * @param userId is the unique user identifier in client application
	 * @return user referenced by @userId
	 * @see Participant
	 */
	User get(String userId);

	boolean exists(String userId);
}