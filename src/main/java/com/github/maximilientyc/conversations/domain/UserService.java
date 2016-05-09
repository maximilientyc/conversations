package com.github.maximilientyc.conversations.domain;

/**
 * Created by @maximilientyc on 09/05/2016.
 */
public interface UserService {

	/**
	 * Method used to get logged in userId. This method needs to be implemented
	 * in order to retrieve the userId of logged in User from client application security context.
	 *
	 * @return userId referencing logged in user from client security context
	 */
	public String getLoggedInUserId();

}
