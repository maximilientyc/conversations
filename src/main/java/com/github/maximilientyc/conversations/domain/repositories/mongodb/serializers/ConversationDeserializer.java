package com.github.maximilientyc.conversations.domain.repositories.mongodb.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.maximilientyc.conversations.domain.Conversation;
import com.github.maximilientyc.conversations.domain.Participant;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

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

		JsonNode lastActiveOnNode = node.get("lastActiveOn");
		if (lastActiveOnNode != null) {
			String lastActiveOnAsString = lastActiveOnNode.textValue();
			ResourceBundle formatsProperties = ResourceBundle.getBundle("formats");
			DateFormat dateFormat = new SimpleDateFormat(
					formatsProperties.getString("dateFormat.postedOn.javaToJson")
			);
			Date lastActiveOn = null;
			try {
				lastActiveOn = dateFormat.parse(lastActiveOnAsString);
			} catch (ParseException e) {
				throw new IllegalStateException(e);
			}
			conversation.setLastActiveOn(lastActiveOn);
		}

		return conversation;
	}
}
