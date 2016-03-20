package com.tipi.conversations.infrastructure.mongodb.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tipi.conversations.domain.Message;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by @maximilientyc on 19/02/2016.
 */
public class MessageSerializer extends JsonSerializer<Message> {

	@Override
	public void serialize(Message message, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("messageId", message.getMessageId());
		jsonGenerator.writeStringField("content", message.getContent());

		ResourceBundle formatsProperties = ResourceBundle.getBundle("formats", Locale.ENGLISH);
		DateFormat dateFormat = new SimpleDateFormat(
				formatsProperties.getString("dateFormat.postedOn.javaToJson")
		);

		jsonGenerator.writeStringField("postedOn", dateFormat.format(message.getPostedOn()));
		jsonGenerator.writeObjectField("postedBy", message.getPostedBy());
		jsonGenerator.writeEndObject();
	}
}
