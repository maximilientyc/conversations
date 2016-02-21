package com.tipi.conversations.infrastructure.conversations;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.tipi.conversations.api.conversations.CreateConversationCommand;
import com.tipi.conversations.domain.conversations.*;
import com.tipi.conversations.domain.users.UserRepository;
import com.tipi.conversations.infrastructure.conversations.mongodb.MongoDbConversationRepository;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by @maximilientyc on 08/02/2016.
 */
public class ConversationMongoDbIntegrationTest {

	@Rule
	public ExpectedException expectedException;
	private final ConversationFactory conversationFactory;
	private final MessageFactory messageFactory;
	private final ParticipantFactory participantFactory;
	private final UserRepository userRepository;
	private ConversationRepository conversationRepository;

	public ConversationMongoDbIntegrationTest() throws IOException {
		ConversationService conversationService = new ConversationService();
		conversationFactory = new ConversationFactory(conversationService);
		messageFactory = new MessageFactory(conversationService);
		participantFactory = new ParticipantFactory(conversationService);
		userRepository = new SampleUserRepository();

		MongoDatabase mongoDatabase = prepareMongoDatabase();
		conversationRepository = new MongoDbConversationRepository(mongoDatabase);
		expectedException = ExpectedException.none();

	}

	private MongoDatabase prepareMongoDatabase() throws IOException {
		MongodForTestsFactory factory = MongodForTestsFactory.with(Version.Main.PRODUCTION);
		MongoClient mongo = factory.newMongo();
		return mongo.getDatabase("conversations-test-#" + UUID.randomUUID());
	}

	@Test
	public void should_contain_newly_created_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// when
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(conversation, conversationRepository);
		createConversationCommand.execute();

		// then
		boolean conversationExists = conversationRepository.exists(conversation);
		assertThat(conversationExists).isTrue();
	}

	@Test
	public void should_return_exactly_the_same_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		conversation.postMessage(messageFactory.buildMessage().setPostedBy(maximilien).setContent("What is wrong with this piece of code ?"));

		// when
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(conversation, conversationRepository);
		createConversationCommand.execute();

		// then
		Conversation conversationFromMongoDb = conversationRepository.get(conversation.getConversationId());
		assertThat(conversation.getConversationId()).isEqualTo(conversationFromMongoDb.getConversationId());
		assertThat(conversation.countParticipants()).isEqualTo(conversationFromMongoDb.countParticipants());
		for (Participant participant : conversation.getParticipants()) {
			assertThat(conversationFromMongoDb.getParticipants().contains(participant)).isTrue();
		}
		for (Message message : conversation.getMessages()) {
			assertThat(conversationFromMongoDb.getMessages().contains(message)).isTrue();
		}
	}

	@Test
	public void should_return_an_error_when_conversation_is_retreived_using_a_null_conversation_id() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Conversation Id cannot be empty.");

		// when
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(conversation, conversationRepository);
		createConversationCommand.execute();
		Conversation conversationFromMongoDb = conversationRepository.get(null);
	}
}
