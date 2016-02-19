package com.tipi.conversations.infrastructure.conversations.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tipi.conversations.domain.conversations.Conversation;
import com.tipi.conversations.domain.conversations.Participant;

import java.io.IOException;

/**
 * Created by Maximilien on 19/02/2016.
 */
public class ParticipantSerializer extends JsonSerializer<Participant> {

	@Override
	public void serialize(Participant participant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeObjectField("user", participant.getUser());
		jsonGenerator.writeEndObject();
	}
}
