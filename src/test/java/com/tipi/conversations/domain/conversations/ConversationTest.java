package com.tipi.conversations.domain.conversations;

import org.assertj.core.api.Assertions;
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
		conversationService = new ConversationService();
		conversationFactory = new ConversationFactory(conversationService);
		messageFactory = new MessageFactory(conversationService);
		participantFactory = new ParticipantFactory(conversationService);
		expectedException = ExpectedException.none();
	}

	@Test
	public void should_contain_at_least_two_participants_when_new() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");

		// when
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// then
		assertThat(conversation.countParticipants()).isGreaterThanOrEqualTo(2);
	}

	@Test
	public void should_not_contain_messages_when_new() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// then
		assertThat(conversation.countMessages()).isEqualTo(0);
	}

	@Test
	public void should_contain_one_message() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// when
		Message message = messageFactory.buildMessage().setContent("This is the message content !");
		conversation.postMessage(message);

		// then
		assertThat(conversation.countMessages()).isEqualTo(1);
	}

	@Test
	public void should_contain_two_messages() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// when
		Message firstMessage = messageFactory.buildMessage().setContent("This is the first message content !");
		conversation.postMessage(firstMessage);
		Message secondMessage = messageFactory.buildMessage().setContent("This is the second message content !");
		conversation.postMessage(secondMessage);

		// then
		assertThat(conversation.countMessages()).isEqualTo(2);
	}

	@Test
	public void should_contain_a_message_posted_by_maximilien() {
		// given
		Participant maximilien = participantFactory.buildParticipant().setName("maximilien");
		Participant bob = participantFactory.buildParticipant().setName("bob");
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// when
		Message firstMessage = messageFactory.buildMessage().setContent("This is the first message content !").setPostedBy(maximilien);
		conversation.postMessage(firstMessage);

		// then
		Message message = conversation.getMessage(firstMessage.getMessageId());
		assertThat(message.postedBy().getName()).isEqualTo("maximilien");
	}

}
