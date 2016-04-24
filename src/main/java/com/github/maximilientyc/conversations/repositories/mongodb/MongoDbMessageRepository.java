package com.github.maximilientyc.conversations.repositories.mongodb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.github.maximilientyc.conversations.domain.Message;
import com.github.maximilientyc.conversations.domain.MessageRepository;
import com.github.maximilientyc.conversations.domain.MessageSearchCriteria;
import com.github.maximilientyc.conversations.repositories.mongodb.serializers.MessageDeserializer;
import com.github.maximilientyc.conversations.repositories.mongodb.serializers.MessageSerializer;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by @maximilientyc on 20/03/2016.
 */
public class MongoDbMessageRepository implements MessageRepository {

	private final MongoCollection<Document> messageCollection;
	private final ObjectMapper messageObjectMapper;

	public MongoDbMessageRepository(MongoDatabase mongoDatabase) {
		this.messageCollection = mongoDatabase.getCollection("messages");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		SimpleModule customSerializerModule = new SimpleModule();
		customSerializerModule.addSerializer(Message.class, new MessageSerializer());
		customSerializerModule.addDeserializer(Message.class, new MessageDeserializer());
		objectMapper.registerModule(customSerializerModule);

		this.messageObjectMapper = objectMapper;
	}

	@Override
	public void add(Message message) {
		try {
			insertOneMessage(message);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private void insertOneMessage(Message message) throws JsonProcessingException {
		String messageAsJson = messageObjectMapper.writeValueAsString(message);
		Document document = Document.parse(messageAsJson);
		messageCollection.insertOne(document);
	}

	@Override
	public boolean exists(Message message) {
		return false;
	}

	@Override
	public Message get(String messageId) {
		return null;
	}

	@Override
	public long count(MessageSearchCriteria criteria) {
		long messageCount = messageCollection.count(eq("conversationId", criteria.getConversationId()));
		return messageCount;
	}
}
