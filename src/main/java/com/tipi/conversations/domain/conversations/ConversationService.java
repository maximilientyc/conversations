package com.tipi.conversations.domain.conversations;

import java.util.UUID;

/**
 * Created by @maximilientyc on 03/01/2016.
 */
public class ConversationService {

	public String getNextConversationId() {
		return UUID.randomUUID().toString();
	}

	public String getNextMessageId() {
		return UUID.randomUUID().toString();
	}

}
