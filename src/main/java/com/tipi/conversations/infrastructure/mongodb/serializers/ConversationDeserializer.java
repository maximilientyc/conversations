package com.tipi.conversations.infrastructure.mongodb.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tipi.conversations.domain.Conversation;
import com.tipi.conversations.domain.Message;
import com.tipi.conversations.domain.Participant;

import java.io.IOException;

/**
 * Created by @maximilientyc on 14/02/2016.
 */
public class ConversationDeserializer extends JsonDeserializer<Conversation> {

	@Override
	public Conversation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		String conversationId = node.get("conversationId").textValue();
		Conversation conversation = new Conversation(conversationId);

		JsonNode participantsNode = node.get("participants");
		for (JsonNode participantNode : participantsNode) {
			Participant participant = participantNode.traverse(jsonParser.getCodec()).readValueAs(Participant.class);
			conversation.addParticipant(participant);
		}

		JsonNode messagesNode = node.get("messages");
		for (JsonNode messageNode : messagesNode) {
			Message message = messageNode.traverse(jsonParser.getCodec()).readValueAs(Message.class);
			conversation.postMessage(message);
		}

		return conversation;
	}
}
