package com.tipi.conversations.infrastructure.conversations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tipi.conversations.domain.conversations.Conversation;

import java.io.IOException;

/**
 * Created by Maximilien on 14/02/2016.
 */
public class ConversationDeserializer extends JsonDeserializer<Conversation> {

	@Override
	public Conversation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		String conversationId = node.get("conversationId").textValue();
		Conversation conversation = new Conversation(conversationId);
		return conversation;
	}
}
