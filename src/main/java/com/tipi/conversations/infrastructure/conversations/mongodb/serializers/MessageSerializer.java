package com.tipi.conversations.infrastructure.conversations.mongodb.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tipi.conversations.domain.conversations.Message;

import java.io.IOException;

/**
 * Created by @maximilientyc on 19/02/2016.
 */
public class MessageSerializer extends JsonSerializer<Message> {

	@Override
	public void serialize(Message message, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("messageId", message.getMessageId());
		jsonGenerator.writeStringField("content", message.getContent());
		jsonGenerator.writeNumberField("postedOn", message.getPostedOn().getTime());
		jsonGenerator.writeObjectField("postedBy", message.getPostedBy());
		jsonGenerator.writeEndObject();
	}
}
