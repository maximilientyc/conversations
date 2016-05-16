package com.github.maximilientyc.conversations.domain.repositories;

import com.github.maximilientyc.conversations.domain.Message;
import com.github.maximilientyc.conversations.domain.MessageSearchCriteria;

/**
 * Created by @maximilientyc on 20/03/2016.
 */
public interface MessageRepository {

	void add(Message message);

	boolean exists(Message message);

	Message get(String messageId);

	long count(MessageSearchCriteria criteria);
}
