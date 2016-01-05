package com.tipi.conversations.infrastructure.sequences;

/**
 * Created by Maximilien on 05/01/2016.
 */
public class Sequences {

	private int currentUserId = 0;

	public String getNextUserId() {
		currentUserId = currentUserId + 1;
		return String.valueOf(currentUserId);
	}

}
