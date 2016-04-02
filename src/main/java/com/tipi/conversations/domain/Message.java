package com.tipi.conversations.domain;

import java.util.Date;

/**
 * Created by @maximilientyc on 10/01/2016.
 */
public class Message {

	private final String messageId;
	private final Date postedOn;
	private Participant postedBy;
	private String conversationId;
	private String content;

	public Message(String messageId, Date postedOn) {
		this.messageId = messageId;
		this.postedOn = postedOn;
	}

	public String getMessageId() {
		return messageId;
	}

	public String getConversationId() {
		return conversationId;
	}

	public Message setConversationId(String conversationId) {
		this.conversationId = conversationId;
		return this;
	}

	public String getContent() {
		return content;
	}

	public Message setContent(String content) {
		this.content = content;
		return this;
	}

	public Participant getPostedBy() {
		return postedBy;
	}

	public Message setPostedBy(Participant postedBy) {
		this.postedBy = postedBy;
		return this;
	}

	public Date getPostedOn() {
		return postedOn;
	}

}
