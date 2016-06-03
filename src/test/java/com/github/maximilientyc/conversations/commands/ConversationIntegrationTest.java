package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.*;
import com.github.maximilientyc.conversations.domain.repositories.ConversationRepository;
import com.github.maximilientyc.conversations.domain.repositories.MessageRepository;
import com.github.maximilientyc.conversations.domain.repositories.UserRepository;
import com.github.maximilientyc.conversations.domain.services.ConversationService;
import com.github.maximilientyc.conversations.domain.services.UserService;
import com.github.maximilientyc.conversations.infrastructure.searches.PaginatedList;
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
public abstract class ConversationIntegrationTest {

	@Rule
	public ExpectedException expectedException;
	public MessageRepository messageRepository;
	public ConversationRepository conversationRepository;
	private ConversationService conversationService;
	private ConversationFactory conversationFactory;
	private ParticipantFactory participantFactory;
	private UserRepository userRepository;
	private UserService userService;
	private MessageFactory messageFactory;

	public ConversationIntegrationTest() {
		expectedException = ExpectedException.none();
	}

	@Before
	public void initComponents() {
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

		Mockito.when(userService.getLoggedInUserId()).thenReturn("max");
	}

	public void prepareRepositories() throws Exception {
		conversationRepository = new SampleConversationRepository();
		messageRepository = new SampleMessageRepository();
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
		UpdateConversationParticipantsCommand updateConversationParticipantsCommand = new UpdateConversationParticipantsCommand(conversationId, userIdSet, conversationFactory, participantFactory, conversationRepositorySpy, userService);
		Mockito.when(conversationRepositorySpy.exists(conversationId)).thenReturn(false);
		updateConversationParticipantsCommand.execute();
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
		UpdateConversationParticipantsCommand updateConversationParticipantsCommand = new UpdateConversationParticipantsCommand(conversationId, userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		updateConversationParticipantsCommand.execute();

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
	public void should_not_contain_removed_participant_after_update() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");
		userIdSet.add("alice");
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		Conversation conversation = createConversationCommand.execute();
		String conversationId = conversation.getConversationId();

		// when
		userIdSet.remove("alice");
		UpdateConversationParticipantsCommand updateConversationParticipantsCommand = new UpdateConversationParticipantsCommand(conversationId, userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		updateConversationParticipantsCommand.execute();

		// then
		Conversation conversationFromRepository = conversationRepository.get(conversationId);
		boolean aliceIsFound = false;
		for (Participant participant : conversationFromRepository.getParticipants()) {
			String userId = participant.getUser().getUserId();
			if (userId.equals("alice")) {
				aliceIsFound = true;
			}
		}
		assertThat(aliceIsFound).isFalse();
	}

	@Test
	public void should_return_an_error_when_creating_a_conversation_with_less_than_2_participants() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");

		// then
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot create conversation, reason: not enough participants.");

		// when
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
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

		// then
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Current logged in user '" + loggedInUserId + "' is not a conversation member.");

		// when
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
	}


	@Test
	public void should_return_the_two_conversations_where_max_is_a_participant() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");

		// when
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		createConversationCommand.execute();
		createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		createConversationCommand.execute();

		// then
		ConversationSearchCriteria conversationSearchCriteria = new ConversationSearchCriteria(0, 5);
		conversationSearchCriteria.setUserId("max");
		conversationSearchCriteria.setSortDirection("desc");
		conversationSearchCriteria.setSortCriteria("lastActiveOn");
		PaginatedList<Conversation> conversationPaginatedList = conversationRepository.find(conversationSearchCriteria);
		assertThat(conversationPaginatedList.getTotalRowCount()).isEqualTo(2);
	}

	@Test
	public void should_update_conversation_last_active_on_when_a_message_is_posted() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");

		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		Conversation conversation = createConversationCommand.execute();

		// when
		Message message = messageFactory.buildMessage().setConversationId(conversation.getConversationId()).setContent("Hello ! How are you all ?)").setPostedBy(conversation.getParticipants().get(0));
		PostMessageCommand postFirstMessageCommand = new PostMessageCommand(message, messageRepository, conversationRepository);
		postFirstMessageCommand.execute();

		// then
		Conversation conversationFromRepository = conversationRepository.get(conversation.getConversationId());
		assertThat(conversationFromRepository.getLastActiveOn().equals(message.getPostedOn()));
	}

	@Test
	public void should_return_an_error_when_a_participant_post_a_message_in_a_conversation_he_has_left() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");
		userIdSet.add("alice");

		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		Conversation conversation = createConversationCommand.execute();
		Participant alice = conversation.getParticipant("alice");

		userIdSet.remove("alice");
		UpdateConversationParticipantsCommand updateConversationParticipantsCommand = new UpdateConversationParticipantsCommand(conversation.getConversationId(), userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		updateConversationParticipantsCommand.execute();

		// then
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot post message, reason: not a participant.");

		// when
		Message message = messageFactory.buildMessage()
				.setConversationId(conversation.getConversationId())
				.setContent("What are you doing maximilien next weekend ? ;)")
				.setPostedBy(alice);
		PostMessageCommand postMessageCommand = new PostMessageCommand(message, messageRepository, conversationRepository);
		postMessageCommand.execute();
	}

	@Test
	public void should_contain_one_conversation_when_updated() {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");

		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		Conversation conversation = createConversationCommand.execute();

		// when
		Message message = messageFactory.buildMessage()
				.setConversationId(conversation.getConversationId())
				.setContent("What are you doing max next weekend ? ;)")
				.setPostedBy(conversation.getParticipant("bob"));
		PostMessageCommand postMessageCommand = new PostMessageCommand(message, messageRepository, conversationRepository);
		postMessageCommand.execute();

		// then
		ConversationSearchCriteria conversationSearchCriteria = new ConversationSearchCriteria(0, Integer.MAX_VALUE);
		conversationSearchCriteria.setUserId(userService.getLoggedInUserId());
		conversationSearchCriteria.setSortCriteria("lastActiveOn");
		conversationSearchCriteria.setSortDirection("desc");
		PaginatedList<Conversation> conversations = conversationRepository.find(conversationSearchCriteria);
		assertThat(conversationRepository.count(conversationSearchCriteria)).isEqualTo(1);
	}

	@Test
	public void should_contain_conversation_with_bob_and_max_in_first_position() throws InterruptedException {
		// given
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.add("max");
		userIdSet.add("bob");

		CreateConversationCommand createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		Conversation bobAndMaxConversation = createConversationCommand.execute();

		userIdSet.add("alice");
		userIdSet.remove("bob");
		createConversationCommand = new CreateConversationCommand(userIdSet, conversationFactory, participantFactory, conversationRepository, userService);
		Conversation aliceAndMaxConversation = createConversationCommand.execute();

		// when
		Message message = messageFactory.buildMessage()
				.setConversationId(aliceAndMaxConversation.getConversationId())
				.setContent("What are you doing max next weekend ? ;)")
				.setPostedBy(aliceAndMaxConversation.getParticipant("alice"));
		PostMessageCommand postMessageCommand = new PostMessageCommand(message, messageRepository, conversationRepository);
		postMessageCommand.execute();

		Thread.sleep(100);

		message = messageFactory.buildMessage()
				.setConversationId(bobAndMaxConversation.getConversationId())
				.setContent("What are you doing max next weekend ? ;)")
				.setPostedBy(bobAndMaxConversation.getParticipant("bob"));
		postMessageCommand = new PostMessageCommand(message, messageRepository, conversationRepository);
		postMessageCommand.execute();

		// then
		ConversationSearchCriteria conversationSearchCriteria = new ConversationSearchCriteria(0, Integer.MAX_VALUE);
		conversationSearchCriteria.setUserId(userService.getLoggedInUserId());
		conversationSearchCriteria.setSortCriteria("lastActiveOn");
		conversationSearchCriteria.setSortDirection("desc");
		PaginatedList<Conversation> conversationPaginatedList = conversationRepository.find(conversationSearchCriteria);

		assertThat(conversationPaginatedList.getItemList().get(0).equals(bobAndMaxConversation)).isTrue();
	}
}
