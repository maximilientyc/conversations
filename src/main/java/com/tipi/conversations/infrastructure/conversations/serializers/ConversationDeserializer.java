package com.tipi.conversations.infrastructure.conversations.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tipi.conversations.domain.conversations.Conversation;
import com.tipi.conversations.domain.conversations.Participant;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Maximilien on 14/02/2016.
 */
public class ConversationDeserializer extends JsonDeserializer<Conversation> {

	@Override
	public Conversation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		String conversationId = node.get("conversationId").textValue();
		Conversation conversation = new Conversation(conversationId);

		JsonNode participantsNode = node.get("participants");
		Iterator<JsonNode> participantNodeIterator = participantsNode.iterator();
		while (participantNodeIterator.hasNext()) {
			JsonNode participantNode = participantNodeIterator.next();
			Participant participant = participantNode.traverse(jsonParser.getCodec()).readValueAs(Participant.class);
			conversation.addParticipant(participant);
		}

		return conversation;
	}
}
