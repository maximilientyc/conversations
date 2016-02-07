package com.tipi.conversations.domain.conversations;

import com.tipi.conversations.domain.users.UserRepository;
import com.tipi.conversations.execution.conversations.CreateConversationCommand;
import com.tipi.conversations.execution.conversations.UpdateConversationCommand;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

/**
 * Created by Maximilien on 07/02/2016.
 */
public class ConversationIntegrationTest {

	private ConversationService conversationService;
	private ConversationFactory conversationFactory;
	private MessageFactory messageFactory;
	private ParticipantFactory participantFactory;
	private UserRepository userRepository;
	private ConversationRepository conversationRepository;

	@Rule
	public ExpectedException expectedException;

	public ConversationIntegrationTest() {
		conversationService = new ConversationService();
		conversationFactory = new ConversationFactory(conversationService);
		messageFactory = new MessageFactory(conversationService);
		participantFactory = new ParticipantFactory(conversationService);
		userRepository = new UserRepositoryTest();
		conversationRepository = new ConversationRepositoryTest();
		expectedException = ExpectedException.none();
	}

	@Test
	public void should_properly_call_conversation_repository_when_creating_a_new_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		ConversationRepository conversationRepositorySpy = Mockito.spy(conversationRepository);
		Mockito.when(conversationRepositorySpy.exists(conversation)).thenReturn(false);
		CreateConversationCommand command = new CreateConversationCommand(conversation, conversationRepositorySpy);

		// when
		command.execute();

		// then
		Mockito.verify(conversationRepositorySpy).add(conversation);
	}

	@Test
	public void should_properly_call_conversation_repository_when_updating_a_new_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		ConversationRepository conversationRepositorySpy = Mockito.spy(conversationRepository);
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(conversation, conversationRepositorySpy);
		Mockito.when(conversationRepositorySpy.exists(conversation)).thenReturn(false);
		createConversationCommand.execute();

		// when
		conversation.addParticipant(participantFactory.buildParticipant(userRepository.get("alice")));
		UpdateConversationCommand updateConversationCommand = new UpdateConversationCommand(conversation, conversationRepositorySpy);
		Mockito.when(conversationRepositorySpy.exists(conversation)).thenReturn(true);
		updateConversationCommand.execute();

		// then
		Mockito.verify(conversationRepositorySpy).update(conversation);
	}

	@Test
	public void should_return_an_error_when_updating_a_non_existing_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot update conversation, reason: a conversation with id '" + conversation.getConversationId() + "' does not exist.");

		// when
		ConversationRepository conversationRepositorySpy = Mockito.spy(conversationRepository);
		UpdateConversationCommand updateConversationCommand = new UpdateConversationCommand(conversation, conversationRepositorySpy);
		Mockito.when(conversationRepositorySpy.exists(conversation)).thenReturn(false);
		updateConversationCommand.execute();
	}

	@Test
	public void should_return_an_error_when_creating_an_already_existing_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		ConversationRepository conversationRepositorySpy = Mockito.spy(conversationRepository);

		CreateConversationCommand createConversationCommand = new CreateConversationCommand(conversation, conversationRepositorySpy);
		Mockito.when(conversationRepositorySpy.exists(conversation)).thenReturn(false);
		createConversationCommand.execute();

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot create conversation, reason: a conversation with id '" + conversation.getConversationId() + "' already exists.");

		// when
		Mockito.when(conversationRepositorySpy.exists(conversation)).thenReturn(true);
		createConversationCommand.execute();
	}

}
