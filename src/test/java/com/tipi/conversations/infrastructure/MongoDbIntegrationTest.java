package com.tipi.conversations.infrastructure;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.tipi.conversations.commands.CreateConversationCommand;
import com.tipi.conversations.domain.*;
import com.tipi.conversations.infrastructure.mongodb.MongoDbConversationRepository;
import com.tipi.conversations.infrastructure.mongodb.MongoDbMessageRepository;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by @maximilientyc on 08/02/2016.
 */
public class MongoDbIntegrationTest {

	private final ConversationService conversationService;
	private final ConversationFactory conversationFactory;
	private final MessageFactory messageFactory;
	private final ParticipantFactory participantFactory;
	private final UserRepository userRepository;
	private final ConversationRepository conversationRepository;
	private final MessageRepository messageRepository;
	@Rule
	public ExpectedException expectedException;

	public MongoDbIntegrationTest() throws IOException {
		MongoDatabase mongoDatabase = prepareMongoDatabase();
		conversationRepository = new MongoDbConversationRepository(mongoDatabase);
		messageRepository = new MongoDbMessageRepository(mongoDatabase);
		expectedException = ExpectedException.none();

		conversationService = new ConversationService(conversationRepository, messageRepository);
		conversationFactory = new ConversationFactory(conversationService);
		messageFactory = new MessageFactory(conversationService);
		userRepository = new SampleUserRepository();
		participantFactory = new ParticipantFactory(userRepository);
	}

	private MongoDatabase prepareMongoDatabase() throws IOException {
		MongodForTestsFactory factory = MongodForTestsFactory.with(Version.Main.PRODUCTION);
		MongoClient mongo = factory.newMongo();
		return mongo.getDatabase("conversations-test-#" + UUID.randomUUID());
	}

	@Test
	public void should_return_exactly_the_same_conversation() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository);

		// when
		Conversation conversation = createConversationCommand.execute();
		String conversationId = conversation.getConversationId();

		// then
		Conversation conversationFromMongoDb = conversationRepository.get(conversationId);
		assertThat(conversationId).isEqualTo(conversationFromMongoDb.getConversationId());
		assertThat(2).isEqualTo(conversationFromMongoDb.countParticipants());

		boolean maxIsFound = false;
		boolean bobIsFound = false;
		for (Participant participant : conversationFromMongoDb.getParticipants()) {
			String userId = participant.getUser().getUserId();
			if (userId.equals("max")) {
				maxIsFound = true;
			} else if (userId.equals("bob")) {
				bobIsFound = true;
			}
		}
		assertThat(maxIsFound && bobIsFound).isTrue();
	}

	@Test
	public void should_return_an_error_when_conversation_is_retrieved_using_a_null_conversation_id() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository);

		// then
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Conversation Id cannot be empty.");

		// when
		Conversation conversation = createConversationCommand.execute();
		String conversationId = conversation.getConversationId();
		Conversation conversationFromMongoDb = conversationRepository.get(null);
	}
}
