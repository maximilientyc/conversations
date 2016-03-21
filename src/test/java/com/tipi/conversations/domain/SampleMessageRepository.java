package com.tipi.conversations.domain;

/**
 * Created by @maximilientyc on 20/03/2016.
 */
public class SampleMessageRepository implements MessageRepository {

	@Override
	public void add(Message message) {

	}

	@Override
	public boolean exists(Message message) {
		return false;
	}

	@Override
	public Message get(String messageId) {
		return null;
	}

	@Override
	public long count(MessageSearchCriteria criteria) {
		return 0;
	}
}
