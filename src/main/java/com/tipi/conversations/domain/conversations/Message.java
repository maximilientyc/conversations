package com.tipi.conversations.domain.conversations;

import java.util.Date;

/**
 * Created by @maximilientyc on 10/01/2016.
 */
public class Message {

	private final String messageId;
	private String content;
	private final Date postedOn;
	private Participant postedBy;

	public Message(String messageId, Date postedOn) {
		this.messageId = messageId;
		this.postedOn = postedOn;
	}

	public String getMessageId() {
		return messageId;
	}

	public String getContent() {
		return content;
	}

	public Message setContent(String content) {
		this.content = content;
		return this;
	}

	public Date getPostedOn() {
		return postedOn;
	}

	public Participant getPostedBy() {
		return postedBy;
	}

	public Message setPostedBy(Participant postedBy) {
		this.postedBy = postedBy;
		return this;
	}
}
