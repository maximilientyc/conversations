package com.tipi.conversations.infrastructure.mongodb.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tipi.conversations.domain.Participant;
import com.tipi.conversations.domain.User;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by @maximilientyc on 19/02/2016.
 */
public class ParticipantDeserializer extends JsonDeserializer<Participant> {

	@Override
	public Participant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		JsonNode userNode = node.get("user");
		User user = userNode.traverse(jsonParser.getCodec()).readValueAs(User.class);

		ResourceBundle formatsProperties = ResourceBundle.getBundle("formats");
		DateFormat dateFormat = new SimpleDateFormat(
				formatsProperties.getString("dateFormat.postedOn.javaToJson")
		);
		String createdOnAsString = node.get("createdOn").textValue();
		Date createdOn = null;
		try {
			createdOn = dateFormat.parse(createdOnAsString);
		} catch (ParseException e) {
			throw new IllegalStateException(e);
		}

		return new Participant(user, createdOn);
	}
}
