package com.github.maximilientyc.conversations.domain.repositories.mongodb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.maximilientyc.conversations.domain.Conversation;
import com.github.maximilientyc.conversations.domain.ConversationSearchCriteria;
import com.github.maximilientyc.conversations.domain.Participant;
import com.github.maximilientyc.conversations.domain.User;
import com.github.maximilientyc.conversations.domain.repositories.ConversationRepository;
import com.github.maximilientyc.conversations.domain.repositories.mongodb.serializers.*;
import com.github.maximilientyc.conversations.infrastructure.searches.PaginatedList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by @maximilientyc on 10/02/2016.
 */
public class MongoDbConversationRepository implements ConversationRepository {

	private final MongoCollection<Document> conversationCollection;

	private final ObjectMapper conversationObjectMapper;

	public MongoDbConversationRepository(MongoDatabase mongoDatabase) {
		this.conversationCollection = mongoDatabase.getCollection("conversations");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		SimpleModule customSerializerModule = new SimpleModule();
		customSerializerModule.addSerializer(Conversation.class, new ConversationSerializer());
		customSerializerModule.addSerializer(Participant.class, new ParticipantSerializer());
		customSerializerModule.addSerializer(User.class, new UserSerializer());
		customSerializerModule.addDeserializer(Conversation.class, new ConversationDeserializer());
		customSerializerModule.addDeserializer(Participant.class, new ParticipantDeserializer());
		customSerializerModule.addDeserializer(User.class, new UserDeserializer());
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
		conversationCollection.insertOne(document);
	}

	@Override
	public void update(Conversation conversation) {
		try {
			updateOneConversation(conversation);
		} catch (JsonProcessingException e) {

		}
	}

	private void updateOneConversation(Conversation conversation) throws JsonProcessingException {
		String conversationAsJson = conversationObjectMapper.writeValueAsString(conversation);
		Document document = Document.parse(conversationAsJson);

		String conversationId = conversation.getConversationId();
		conversationCollection.findOneAndReplace(eq("conversationId", conversationId), document);
	}

	@Override
	public boolean exists(String conversationId) {
		long conversationCount = conversationCollection.count(eq("conversationId", conversationId));
		return conversationCount > 0;
	}

	@Override
	public Conversation get(String conversationId) {
		if (conversationId == null) {
			throw new IllegalArgumentException("Conversation Id cannot be empty.");
		}
		try {
			return findOneConversation(conversationId);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Conversation findOneConversation(String conversationId) throws IOException {
		FindIterable<Document> documents = conversationCollection.find(eq("conversationId", conversationId));
		Document document = documents.first();
		return conversationObjectMapper.readValue(document.toJson(), Conversation.class);
	}

	private PaginatedList<Conversation> findConversations(ConversationSearchCriteria conversationSearchCriteria) throws IOException {
		int sortDirection = (conversationSearchCriteria.getSortDirection().toLowerCase().equals("asc")) ? 1 : -1;
		FindIterable<Document> documents = conversationCollection.find(eq("participants.user.userId", conversationSearchCriteria.getUserId())).sort(
				new BasicDBObject("lastActiveOn", sortDirection)
		);

		List<Conversation> foundConversationList = new ArrayList<Conversation>();
		for (Document document : documents) {
			Conversation conversation = conversation = conversationObjectMapper.readValue(document.toJson(), Conversation.class);
			foundConversationList.add(conversation);
		}

		PaginatedList<Conversation> conversationPaginatedList = new PaginatedList<Conversation>(foundConversationList.size(), foundConversationList);
		return conversationPaginatedList;
	}

	@Override
	public PaginatedList<Conversation> find(ConversationSearchCriteria conversationSearchCriteria) {
		try {
			return findConversations(conversationSearchCriteria);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long count(ConversationSearchCriteria criteria) {
		long conversationCount = conversationCollection.count();
		return conversationCount;
	}
}
