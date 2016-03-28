package com.tipi.conversations.infrastructure;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.tipi.conversations.api.CreateConversationCommand;
import com.tipi.conversations.api.PostMessageCommand;
import com.tipi.conversations.domain.*;
import com.tipi.conversations.infrastructure.mongodb.MongoDbConversationRepository;
import com.tipi.conversations.infrastructure.mongodb.MongoDbMessageRepository;
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
	public void should_contain_newly_created_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant("max");
		Participant bob = participantFactory.buildParticipant("bob");

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
	public void should_contain_one_message() {
		// given
		Participant maximilien = participantFactory.buildParticipant("max");
		Participant bob = participantFactory.buildParticipant("bob");

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		CreateConversationCommand createConversationCommand = new CreateConversationCommand(conversation, conversationRepository);
		createConversationCommand.execute();

		// when
		Message firstMessage = messageFactory.buildMessage().setConversationId(conversation.getConversationId()).setContent("First message to be stored in mongoDb database :)").setPostedBy(maximilien);
		PostMessageCommand postMessageCommand = new PostMessageCommand(firstMessage, messageRepository, conversationRepository);
		postMessageCommand.execute();

		// then
		Conversation conversationFromMongoDb = conversationRepository.get(conversation.getConversationId());
		long messageCount = conversationService.countMessages(conversation.getConversationId());
		assertThat(messageCount).isEqualTo(new Long(1));
	}

	@Test
	public void should_return_exactly_the_same_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant("max");
		Participant bob = participantFactory.buildParticipant("bob");

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);


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
	}

	@Test
	public void should_return_an_error_when_conversation_is_retrieved_using_a_null_conversation_id() {
		// given
		Participant maximilien = participantFactory.buildParticipant("max");
		Participant bob = participantFactory.buildParticipant("bob");

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
