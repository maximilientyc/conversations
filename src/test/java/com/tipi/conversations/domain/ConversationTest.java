package com.tipi.conversations.domain;

import com.tipi.conversations.api.CreateConversationCommand;
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

	public ConversationTest() {
		conversationService = new ConversationService();
		conversationFactory = new ConversationFactory(conversationService);
		messageFactory = new MessageFactory(conversationService);
		participantFactory = new ParticipantFactory(conversationService);
		userRepository = new SampleUserRepository();
		conversationRepository = new SampleConversationRepository();
		expectedException = ExpectedException.none();
	}

	@Test
	public void should_not_contain_messages_when_new() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		// when
		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// then
		assertThat(conversation.countMessages()).isEqualTo(0);
	}

	@Test
	public void should_contain_one_message() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// when
		Message message = messageFactory.buildMessage().setContent("This is the message content !").setPostedBy(maximilien);
		conversation.postMessage(message);

		// then
		assertThat(conversation.countMessages()).isEqualTo(1);
	}

	@Test
	public void should_contain_two_messages() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// when
		Message firstMessage = messageFactory.buildMessage().setContent("This is the first message content !").setPostedBy(maximilien);
		conversation.postMessage(firstMessage);
		Message secondMessage = messageFactory.buildMessage().setContent("This is the second message content !").setPostedBy(bob);
		conversation.postMessage(secondMessage);

		// then
		assertThat(conversation.countMessages()).isEqualTo(2);
	}

	@Test
	public void should_return_an_error_when_creating_a_conversation_with_less_than_2_participants() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien);

		ConversationRepository conversationRepositorySpy = Mockito.spy(conversationRepository);
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(conversation, conversationRepositorySpy);
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot create conversation, reason: not enough getParticipants.");

		// when
		Mockito.when(conversationRepositorySpy.exists(conversation)).thenReturn(false);
		createConversationCommand.execute();
	}

	@Test
	public void should_return_an_error_when_a_participant_leaves_a_two_participants_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot leave conversation, reason: not enough getParticipants.");

		// when
		conversation.removeParticipant(maximilien);
	}

	@Test
	public void should_contain_two_participants_when_a_participant_leaves_a_three_participant_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));
		Participant alice = participantFactory.buildParticipant(userRepository.get("alice"));

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
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));
		Participant alice = participantFactory.buildParticipant(userRepository.get("alice"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob)
				.addParticipant(alice);

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot post message, reason: not a participant.");

		// when
		conversation.removeParticipant(alice);
		Message message = messageFactory.buildMessage().setContent("What are you doing maximilien next weekend ? ;)").setPostedBy(alice);
		conversation.postMessage(message);
	}

	@Test
	public void should_maintain_chronology_between_messages_inside_a_conversation() {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		// when
		Message firstMessage = messageFactory.buildMessage().setContent("Hello ! How are you all ?)").setPostedBy(maximilien);
		conversation.postMessage(firstMessage);
		Message secondMessage = messageFactory.buildMessage().setContent("I'm fine, thank you max.").setPostedBy(bob);
		conversation.postMessage(secondMessage);

		// then
		assertThat(firstMessage.getPostedOn()).isBeforeOrEqualsTo(secondMessage.getPostedOn());
	}

	@Test
	public void should_maintain_chronology_between_participant_arrival_inside_a_conversation() throws InterruptedException {
		// given
		Participant maximilien = participantFactory.buildParticipant(userRepository.get("max"));
		Participant bob = participantFactory.buildParticipant(userRepository.get("bob"));

		Conversation conversation = conversationFactory.buildConversation()
				.addParticipant(maximilien)
				.addParticipant(bob);

		Message firstMessage = messageFactory.buildMessage().setContent("Hello ! How are you all ?)").setPostedBy(maximilien);
		conversation.postMessage(firstMessage);
		Message secondMessage = messageFactory.buildMessage().setContent("I'm fine, thank you max.").setPostedBy(bob);
		conversation.postMessage(secondMessage);

		// when
		Thread.sleep(1000);
		Participant alice = participantFactory.buildParticipant(userRepository.get("alice"));
		conversation.addParticipant(alice);

		// then
		assertThat(alice.getCreatedOn()).isAfter(bob.getCreatedOn());
		assertThat(alice.getCreatedOn()).isAfter(maximilien.getCreatedOn());
	}

}