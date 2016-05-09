package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public class ConversationIntegrationTest {

	@Rule
	public final ExpectedException expectedException;
	private final ConversationService conversationService;
	private final ConversationFactory conversationFactory;
	private final ParticipantFactory participantFactory;
	private final UserRepository userRepository;
	private final UserService userService;
	private final MessageFactory messageFactory;
	public MessageRepository messageRepository;
	public ConversationRepository conversationRepository;

	public ConversationIntegrationTest() {
		try {
			prepareRepositories();
		} catch (Exception e) {
			e.printStackTrace();
		}

		conversationService = new ConversationService(conversationRepository, messageRepository);
		userRepository = new SampleUserRepository();
		userService = Mockito.mock(SampleUserService.class);
		participantFactory = new ParticipantFactory(userRepository);
		conversationFactory = new ConversationFactory(conversationService);
		messageFactory = new MessageFactory(conversationService);
		expectedException = ExpectedException.none();
	}

	public void prepareRepositories() throws Exception {
		conversationRepository = new SampleConversationRepository();
		messageRepository = new SampleMessageRepository();
	}

	@Before
	public void prepareLoggedInUser() {
		Mockito.when(userService.getLoggedInUserId()).thenReturn("max");
	}

	@Test
	public void should_contain_one_conversation() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);

		// when
		Conversation conversation = createConversationCommand.execute();
		String conversationId = conversation.getConversationId();

		// then
		long conversationCount = conversationService.countConversations();
		assertThat(conversationCount).isEqualTo(1);
	}

	@Test
	public void should_contain_newly_created_conversation() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);

		// when
		Conversation conversation = createConversationCommand.execute();
		String conversationId = conversation.getConversationId();

		// then
		boolean conversationExists = conversationRepository.exists(conversationId);
		assertThat(conversationExists).isTrue();
	}

	@Test
	public void should_return_exactly_the_same_conversation() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);

		// when
		Conversation conversation = createConversationCommand.execute();
		String conversationId = conversation.getConversationId();

		// then
		Conversation conversationFromRepository = conversationRepository.get(conversationId);
		assertThat(conversationId).isEqualTo(conversationFromRepository.getConversationId());
		assertThat(2).isEqualTo(conversationFromRepository.countParticipants());

		boolean maxIsFound = false;
		boolean bobIsFound = false;
		for (Participant participant : conversationFromRepository.getParticipants()) {
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
	public void should_contain_one_message() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		Conversation conversation = createConversationCommand.execute();
		String conversationId = conversation.getConversationId();

		// when
		Message firstMessage = messageFactory.buildMessage().setConversationId(conversationId).setContent("First message to be stored in mongoDb database :)").setPostedBy(conversation.getParticipants().get(0));
		PostMessageCommand postMessageCommand = new PostMessageCommand(firstMessage, messageRepository, conversationRepository);
		postMessageCommand.execute();

		// then
		long messageCount = conversationService.countMessages(conversationId);
		assertThat(messageCount).isEqualTo(new Long(1));
	}

	@Test
	public void should_return_an_error_when_updating_a_non_existing_conversation() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");
		String conversationId = "abcdef";

		// then
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot update conversation, reason: a conversation with id '" + conversationId + "' does not exist.");

		// when
		ConversationRepository conversationRepositorySpy = Mockito.spy(conversationRepository);
		UpdateConversationCommand updateConversationCommand = new UpdateConversationCommand(conversationId, userIdSet, conversationFactory, participantFactory, conversationRepositorySpy);
		Mockito.when(conversationRepositorySpy.exists(conversationId)).thenReturn(false);
		updateConversationCommand.execute();
	}

	@Test
	public void should_contain_newly_added_participant_after_update() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		Conversation conversation = createConversationCommand.execute();
		String conversationId = conversation.getConversationId();

		// when
		userIdSet.add("alice");
		UpdateConversationCommand updateConversationCommand = new UpdateConversationCommand(conversationId, userIdSet, conversationFactory, participantFactory, conversationRepository);
		updateConversationCommand.execute();

		// then
		Conversation conversationFromRepository = conversationRepository.get(conversationId);
		boolean aliceIsFound = false;
		for (Participant participant : conversationFromRepository.getParticipants()) {
			String userId = participant.getUser().getUserId();
			if (userId.equals("alice")) {
				aliceIsFound = true;
			}
		}
		assertThat(aliceIsFound).isTrue();
	}

	@Test
	public void should_return_an_error_when_creating_a_conversation_with_less_than_2_participants() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);

		// then
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot create conversation, reason: not enough participants.");

		// when
		createConversationCommand.execute();
	}

	@Test
	public void should_return_an_error_when_conversation_is_retrieved_using_a_null_conversation_id() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);

		// then
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Conversation Id cannot be empty.");

		// when
		Conversation conversation = createConversationCommand.execute();
		String conversationId = conversation.getConversationId();
		conversationRepository.get(null);
	}

	@Test
	public void should_return_an_error_when_creating_a_conversation_without_logged_in_user() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");

		String loggedInUserId = "alice";
		Mockito.when(userService.getLoggedInUserId()).thenReturn(loggedInUserId);

		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);

		// then
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Current logged in user '" + loggedInUserId + "' is not a conversation member.");

		// when
		Conversation conversation = createConversationCommand.execute();
	}

}
