package com.tipi.conversations.infrastructure.mongodb.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tipi.conversations.domain.Message;
import com.tipi.conversations.domain.Participant;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by @maximilientyc on 21/02/2016.
 */
public class MessageDeserializer extends JsonDeserializer<Message> {

	@Override
	public Message deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);

		String messageId = node.get("messageId").textValue();
		String content = node.get("content").textValue();

		ResourceBundle formatsProperties = ResourceBundle.getBundle("formats");
		DateFormat dateFormat = new SimpleDateFormat(
				formatsProperties.getString("dateFormat.postedOn.javaToJson")
		);
		String postedAsString = node.get("postedOn").textValue();
		Date postedOn = null;
		try {
			postedOn = dateFormat.parse(postedAsString);
		} catch (ParseException e) {
			throw new IllegalStateException(e);
		}

		JsonNode postedByJsonNode = node.get("postedBy");
		Participant postedBy = postedByJsonNode.traverse(jsonParser.getCodec()).readValueAs(Participant.class);

		return new Message(messageId, postedOn).setPostedBy(postedBy).setContent(content);
	}
}
