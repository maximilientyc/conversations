package com.tipi.conversations.infrastructure.conversations.mongodb.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tipi.conversations.domain.conversations.Conversation;

import java.io.IOException;

/**
 * Created by @maximilientyc on 14/02/2016.
 */
public class ConversationSerializer extends JsonSerializer<Conversation> {

	@Override
	public void serialize(Conversation conversation, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("conversationId", conversation.getConversationId());
		jsonGenerator.writeObjectField("participants", conversation.getParticipants());
		jsonGenerator.writeObjectField("messages", conversation.getMessages());
		jsonGenerator.writeEndObject();
	}
}
