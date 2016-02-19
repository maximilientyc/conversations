package com.tipi.conversations.infrastructure.conversations.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sun.org.apache.xml.internal.serializer.Serializer;
import com.tipi.conversations.domain.conversations.Message;

import java.io.IOException;

/**
 * Created by Maximilien on 19/02/2016.
 */
public class MessageSerializer extends JsonSerializer<Message> {

	@Override
	public void serialize(Message message, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("messageId", message.getMessageId());
		jsonGenerator.writeStringField("content", message.getContent());
		jsonGenerator.writeNumberField("postedOn", message.getPostedOn().getTime());
		jsonGenerator.writeObjectField("postedBy", message.getPostedBy());
		jsonGenerator.writeEndObject();
	}
}
