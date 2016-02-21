package com.tipi.conversations.infrastructure.conversations.mongodb.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tipi.conversations.domain.users.User;

import java.io.IOException;

/**
 * Created by @maximilientyc on 19/02/2016.
 */
public class UserDeserializer extends JsonDeserializer<User> {

	@Override
	public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		String userId = node.get("userId").textValue();
		String firstName = node.get("firstName").textValue();
		String lastName = node.get("lastName").textValue();
		return new User(userId, firstName, lastName);
	}
}
