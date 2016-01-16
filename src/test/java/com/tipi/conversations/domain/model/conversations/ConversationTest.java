package com.tipi.conversations.domain.model.conversations;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Maximilien on 03/01/2016.
 */
public class ConversationTest {

	private ConversationService conversationService;
	private ConversationFactory conversationFactory;
	private MessageFactory messageFactory;
	private ParticipantFactory participantFactory;

	@Rule
	public ExpectedException expectedException;

	public ConversationTest() {
		expectedException = ExpectedException.none();
	}

	@Before
	public void prepareConversations() {
		conversationService = new ConversationService(new InMemoryConversationsRepository());
		conversationFactory = new ConversationFactory(conversationService);
		messageFactory = new MessageFactory(conversationService);
		participantFactory = new ParticipantFactory(conversationService);
	}

	@Test
	public void should_contain_two_participants() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");

		// when
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);
		conversationService.add(conversation);

		// then
		Conversation storedConversation = conversationService.getByConversationId(conversation.getConversationId());
		assertThat(storedConversation.getParticipants()).hasSize(2);
	}

	@Test
	public void should_contain_one_message() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);
		conversationService.add(conversation);

		// when
		Message message = messageFactory.buildMessage().setContent("This is the message content !");
		conversation.postMessage(message);
		conversationService.update(conversation);

		// then
		Conversation storedConversation = conversationService.getByConversationId(conversation.getConversationId());
		assertThat(storedConversation.getMessages()).hasSize(1);
	}

	@Test
	public void should_contain_two_messages() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);
		conversationService.add(conversation);

		// when
		Message firstMessage = messageFactory.buildMessage().setContent("This is the first message content !");
		conversation.postMessage(firstMessage);
		conversationService.update(conversation);

		Message secondMessage = messageFactory.buildMessage().setContent("This is the second message content !");
		conversation.postMessage(secondMessage);
		conversationService.update(conversation);

		// then
		Conversation storedConversation = conversationService.getByConversationId(conversation.getConversationId());
		assertThat(storedConversation.getMessages()).hasSize(2);
	}

	@Test
	public void should_not_contain_message_with_id_12345() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);
		conversationService.add(conversation);

		// when
		Message message = messageFactory.buildMessage().setContent("This is the message content !");
		conversation.postMessage(message);
		conversationService.update(conversation);

		// then
		Conversation storedConversation = conversationService.getByConversationId(conversation.getConversationId());

		assertThat(storedConversation.getMessage("1234")).isNull();
	}

	@Test
	public void should_contain_a_message_posted_by_maximilien() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);
		conversationService.add(conversation);

		// when
		Message firstMessage = messageFactory.buildMessage().setContent("This is the first message content !").setPostedBy(maximilien);
		conversation.postMessage(firstMessage);
		conversationService.update(conversation);

		// then
		Conversation storedConversation = conversationService.getByConversationId(conversation.getConversationId());
		Message storedMessage = storedConversation.getMessage(firstMessage.getMessageId());
		assertThat(storedMessage.postedBy().getName()).isEqualTo("maximilien");
	}

	@Test
	public void should_second_message_be_most_recent_than_first_message() throws InterruptedException {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);
		conversationService.add(conversation);

		// when
		Message firstMessage = messageFactory.buildMessage().setContent("This is the first message content !");
		conversation.postMessage(firstMessage);
		conversationService.update(conversation);
		Thread.sleep(1);
		Message secondMessage = messageFactory.buildMessage().setContent("This is the second message content !");
		conversation.postMessage(secondMessage);
		conversationService.update(conversation);

		// then
		Conversation storedConversation = conversationService.getByConversationId(conversation.getConversationId());
		Date firstMessagePostedOn = storedConversation.getMessage(firstMessage.getMessageId()).postedOn();
		Date secondMessagePostedOn = storedConversation.getMessage(secondMessage.getMessageId()).postedOn();

		assertThat(firstMessagePostedOn).isBefore(secondMessagePostedOn);
	}

	@Test
	public void should_return_an_error_when_conversation_has_only_one_participant() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot add conversation, reason: not enough participants.");

		// when
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien);

		conversationService.add(conversation);
	}

	@Test
	public void should_return_an_error_when_creating_an_already_existing_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot add conversation, reason: conversation already exists.");

		// when
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		conversationService.add(conversation);
		conversationService.add(conversation);
	}

	@Test
	public void should_return_an_error_when_updating_a_non_existing_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot update conversation, reason: conversation does not exists.");

		// when
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		conversationService.update(conversation);
	}

}
