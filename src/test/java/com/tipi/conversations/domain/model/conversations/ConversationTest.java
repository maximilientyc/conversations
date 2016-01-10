package com.tipi.conversations.domain.model.conversations;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Maximilien on 03/01/2016.
 */
public class ConversationTest {

	private ConversationService conversationService;
	private ConversationFactory conversationFactory;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void prepareConversations() {
		conversationService = new ConversationService(new InMemoryConversationsRepository());
		conversationFactory = new ConversationFactory(conversationService);
	}

	@Test
	public void should_contain_two_participants() {
		// given
		Participant firstParticipant = new Participant();
		Participant secondParticipant = new Participant();

		// when
		Conversation conversation = conversationFactory.createConversation()
				.addParticipant(firstParticipant)
				.addParticipant(secondParticipant);
		conversationService.add(conversation);

		// then
		Conversation storedConversation = conversationService.getByConversationId(conversation.getConversationId());
		assertThat(storedConversation.getParticipants()).hasSize(2);
	}

	@Test
	public void should_return_an_error_when_conversation_has_only_one_participant() {
		// given
		Participant firstParticipant = new Participant();
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot add conversation, reason: not enough participants.");

		// when
		Conversation conversation = conversationFactory.createConversation()
				.addParticipant(firstParticipant);
		conversationService.add(conversation);
	}

	@Test
	public void should_contain_one_message() {
		// given
		Participant firstParticipant = new Participant();
		Participant secondParticipant = new Participant();
		Conversation conversation = conversationFactory.createConversation()
				.addParticipant(firstParticipant)
				.addParticipant(secondParticipant);

		// when
		conversation.postMessage("This is the message content !");
		conversationService.add(conversation);

		// then
		Conversation storedConversation = conversationService.getByConversationId(conversation.getConversationId());
		assertThat(storedConversation.getMessages()).hasSize(1);
	}

}
