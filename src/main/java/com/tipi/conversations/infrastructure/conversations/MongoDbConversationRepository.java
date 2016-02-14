package com.tipi.conversations.infrastructure.conversations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tipi.conversations.domain.conversations.Conversation;
import com.tipi.conversations.domain.conversations.ConversationRepository;
import org.bson.Document;

import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Maximilien on 10/02/2016.
 */
public class MongoDbConversationRepository implements ConversationRepository {

	private MongoCollection<Document> collection;
	private ObjectMapper conversationObjectMapper;

	public MongoDbConversationRepository(MongoDatabase mongoDatabase) {
		this.collection = mongoDatabase.getCollection("conversations");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		SimpleModule customSerializerModule = new SimpleModule();
		customSerializerModule.addSerializer(Conversation.class, new ConversationSerializer());
		customSerializerModule.addDeserializer(Conversation.class, new ConversationDeserializer());
		objectMapper.registerModule(customSerializerModule);
		this.conversationObjectMapper = objectMapper;
	}

	@Override
	public void add(Conversation conversation) {
		try {
			insertOneConversation(conversation);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private void insertOneConversation(Conversation conversation) throws JsonProcessingException {
		String conversationAsJson = conversationObjectMapper.writeValueAsString(conversation);
		Document document = Document.parse(conversationAsJson);
		collection.insertOne(document);
	}

	@Override
	public void update(Conversation conversation) {

	}

	@Override
	public boolean exists(Conversation conversation) {
		long conversationCount = collection.count(eq("conversationId", conversation.getConversationId()));
		return conversationCount > 0;
	}

	@Override
	public Conversation get(String conversationId) {
		try {
			return findOneConversation(conversationId);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private Conversation findOneConversation(String conversationId) throws IOException {
		Document document = collection.findOneAndDelete(eq("conversationId", conversationId));
		Conversation conversation = conversationObjectMapper.readValue(document.toJson(), Conversation.class);
		return conversation;
	}

}
