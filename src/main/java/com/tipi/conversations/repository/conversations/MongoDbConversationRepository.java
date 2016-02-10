package com.tipi.conversations.repository.conversations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tipi.conversations.domain.conversations.Conversation;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Maximilien on 10/02/2016.
 */
public class MongoDbConversationRepository implements ConversationRepository {

	private MongoCollection<Document> collection;

	public MongoDbConversationRepository(MongoDatabase mongoDatabase) {
		this.collection = mongoDatabase.getCollection("conversations");
		if (this.collection == null) {
			mongoDatabase.createCollection("conversations");
		}
	}

	@Override
	public void add(Conversation conversation) {
		collection.insertOne(
				new Document()
						.append("conversationId", conversation.getConversationId())
		);
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
