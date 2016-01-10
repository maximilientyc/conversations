package com.tipi.conversations.domain.model.conversations;

import java.util.Date;

/**
 * Created by Maximilien on 10/01/2016.
 */
public class Message {

	private String content;
	private Date postedOn;

	public Message(String content) {
		this.content = content;
		this.postedOn = new Date();
	}

}
