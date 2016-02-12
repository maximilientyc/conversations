package com.tipi.conversations.infrastructure.conversations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tipi.conversations.domain.conversations.Conversation;
import com.tipi.conversations.domain.conversations.ConversationRepository;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Maximilien on 10/02/2016.
 */
public class MongoDbConversationRepository implements ConversationRepository {

	private MongoCollection<Document> collection;

	public MongoDbConversationRepository(MongoDatabase mongoDatabase) {
		this.collection = mongoDatabase.getCollection("conversations");
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
		ObjectMapper mapper = new ObjectMapper();
		String conversationAsJson = mapper.writeValueAsString(conversation);
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
}
