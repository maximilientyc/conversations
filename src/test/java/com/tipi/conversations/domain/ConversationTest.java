package com.tipi.conversations.domain;

import com.tipi.conversations.commands.PostMessageCommand;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by @maximilientyc on 03/01/2016.
 */
public class ConversationTest {

	@Rule
	public final ExpectedException expectedException;
	private final ConversationService conversationService;
	private final ConversationFactory conversationFactory;
	private final MessageFactory messageFactory;
	private final ParticipantFactory participantFactory;
	private final UserRepository userRepository;
	private final ConversationRepository conversationRepository;
	private final MessageRepository messageRepository;

	public ConversationTest() {
		conversationRepository = Mockito.mock(SampleConversationRepository.class);
		messageRepository = Mockito.mock(SampleMessageRepository.class);
		conversationService = new ConversationService(conversationRepository, messageRepository);
		conversationFactory = new ConversationFactory(conversationService);
		messageFactory = new MessageFactory(conversationService);
		userRepository = new SampleUserRepository();
		participantFactory = new ParticipantFactory(userRepository);
		expectedException = ExpectedException.none();
	}

	@Test
	public void should_not_contain_messages_when_new() {
		// given
		Participant maximilien = participantFactory.buildParticipant("max");
		Participant bob = participantFactory.buildParticipant("bob");

		// when
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// then
		long messageCount = conversationService.countMessages(conversation.getConversationId());
		assertThat(messageCount).isEqualTo(0);
	}


	@Test
	public void should_return_an_error_when_a_participant_leaves_a_two_participants_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant("max");
		Participant bob = participantFactory.buildParticipant("bob");

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// then
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot leave conversation, reason: not enough getParticipants.");

		// when
		conversation.removeParticipant(maximilien);
	}

	@Test
	public void should_contain_two_participants_when_a_participant_leaves_a_three_participant_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant("max");
		Participant bob = participantFactory.buildParticipant("bob");
		Participant alice = participantFactory.buildParticipant("alice");

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob)
				.addParticipant(alice);

		// when
		conversation.removeParticipant(alice);

		// then
		assertThat(conversation.countParticipants()).isEqualTo(2);
	}

	@Test
	public void should_return_an_error_when_a_participant_post_a_message_in_a_conversation_he_has_left() {
		// given
		Participant maximilien = participantFactory.buildParticipant("max");
		Participant bob = participantFactory.buildParticipant("bob");
		Participant alice = participantFactory.buildParticipant("alice");

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob)
				.addParticipant(alice);

		Mockito.when(conversationRepository.get(conversation.getConversationId())).thenReturn(conversation);

		// then
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot post message, reason: not a participant.");

		// when
		conversation.removeParticipant(alice);
		Message message = messageFactory.buildMessage().setConversationId(conversation.getConversationId()).setContent("What are you doing maximilien next weekend ? ;)").setPostedBy(alice);
		PostMessageCommand postMessageCommand = new PostMessageCommand(message, messageRepository, conversationRepository);
		postMessageCommand.execute();
	}

	@Test
	public void should_return_an_error_when_adding_an_already_existing_participant() {
		// given
		Participant maximilien = participantFactory.buildParticipant("max");
		Participant bob = participantFactory.buildParticipant("bob");

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// then
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot add participant, reason: already exists.");

		// when
		Participant maxDuplicate = participantFactory.buildParticipant("max");
		conversation.addParticipant(maxDuplicate);
	}

	@Test
	public void should_maintain_chronology_between_messages_inside_a_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant("max");
		Participant bob = participantFactory.buildParticipant("bob");

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		Mockito.when(conversationRepository.get(conversation.getConversationId())).thenReturn(conversation);

		// when
		Message firstMessage = messageFactory.buildMessage().setConversationId(conversation.getConversationId()).setContent("Hello ! How are you all ?)").setPostedBy(maximilien);
		PostMessageCommand postFirstMessageCommand = new PostMessageCommand(firstMessage, messageRepository, conversationRepository);
		postFirstMessageCommand.execute();

		Message secondMessage = messageFactory.buildMessage().setConversationId(conversation.getConversationId()).setContent("I'm fine, thank you max.").setPostedBy(bob);
		PostMessageCommand postSecondMessageCommand = new PostMessageCommand(secondMessage, messageRepository, conversationRepository);
		postSecondMessageCommand.execute();

		// then
		assertThat(firstMessage.getPostedOn()).isBeforeOrEqualsTo(secondMessage.getPostedOn());
	}

	@Test
	public void should_maintain_chronology_between_participant_arrival_inside_a_conversation() throws InterruptedException {
		// given
		Participant maximilien = participantFactory.buildParticipant("max");
		Participant bob = participantFactory.buildParticipant("bob");

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// when
		Thread.sleep(1000);
		Participant alice = participantFactory.buildParticipant("alice");
		conversation.addParticipant(alice);

		// then
		assertThat(alice.getCreatedOn()).isAfter(bob.getCreatedOn());
		assertThat(alice.getCreatedOn()).isAfter(maximilien.getCreatedOn());
	}

}
