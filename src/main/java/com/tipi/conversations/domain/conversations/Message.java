package com.tipi.conversations.domain.conversations;

import java.util.Date;

/**
 * Created by Maximilien on 10/01/2016.
 */
public class Message {

	private String messageId;
	private String content;
	private Date postedOn;
	private Participant postedBy;

	public Message(String messageId, Date postedOn) {
		this.messageId = messageId;
		this.postedOn = postedOn;
	}

	public Message setContent(String content) {
		this.content = content;
		return this;
	}

	public Message setPostedBy(Participant postedBy) {
		this.postedBy = postedBy;
		return this;
	}

	public Participant postedBy() {
		return postedBy;
	}

	public Date postedOn() {
		return postedOn;
	}
}
