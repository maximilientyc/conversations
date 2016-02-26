package com.tipi.conversations.infrastructure.conversations.mongodb.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tipi.conversations.domain.conversations.Participant;
import com.tipi.conversations.domain.users.User;

import java.io.IOException;

/**
 * Created by @maximilientyc on 19/02/2016.
 */
public class ParticipantDeserializer extends JsonDeserializer<Participant> {

	@Override
	public Participant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		JsonNode userNode = node.get("user");
		User user = userNode.traverse(jsonParser.getCodec()).readValueAs(User.class);
		return new Participant(user);
	}
}
