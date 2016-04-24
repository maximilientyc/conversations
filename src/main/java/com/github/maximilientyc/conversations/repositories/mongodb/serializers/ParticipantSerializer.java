package com.github.maximilientyc.conversations.repositories.mongodb.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.maximilientyc.conversations.domain.Participant;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by @maximilientyc on 19/02/2016.
 */
public class ParticipantSerializer extends JsonSerializer<Participant> {

	@Override
	public void serialize(Participant participant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeObjectField("user", participant.getUser());

		ResourceBundle formatsProperties = ResourceBundle.getBundle("formats", Locale.ENGLISH);
		DateFormat dateFormat = new SimpleDateFormat(
				formatsProperties.getString("dateFormat.postedOn.javaToJson")
		);
		jsonGenerator.writeStringField("createdOn", dateFormat.format(participant.getCreatedOn()));

		jsonGenerator.writeEndObject();
	}
}
