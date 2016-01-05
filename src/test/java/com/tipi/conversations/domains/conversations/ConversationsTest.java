package com.tipi.conversations.domains.conversations;

import com.tipi.conversations.domains.conversations.repositories.InMemoryConversationsRepository;
import com.tipi.conversations.domains.users.User;
import com.tipi.conversations.domains.users.Users;
import com.tipi.conversations.domains.users.repositories.InMemoryUsersRepository;
import com.tipi.conversations.infrastructure.sequences.Sequences;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Maximilien on 03/01/2016.
 */
public class ConversationsTest {

	private Users users;
	private Conversations conversations;
	private Sequences sequences;
	private CrudRepository usersRepository;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void prepareUsers() {
		users = new Users(new InMemoryUsersRepository());
	}

	@Before
	public void prepareConversations() {
		conversations = new Conversations(new InMemoryConversationsRepository());
	}

	@Before
	public void prepareSequences() {
		sequences = new Sequences();
	}

	@Test
	public void should_contain_two_participants() {
		// given
		User firstParticipant = new User(sequences.getNextUserId());
		users.add(firstParticipant);
		User secondParticipant = new User(sequences.getNextUserId());
		users.add(secondParticipant);

		// when
		Conversation conversation = new Conversation(sequences.getNextConversationId());
		conversation.addParticipant(firstParticipant);
		conversation.addParticipant(secondParticipant);
		conversations.add(conversation);

		// then
		Conversation storedConversation = conversations.getByConversationId(conversation.getConversationId());
		assertThat(storedConversation.getParticipants()).hasSize(2);
	}

	@Test
	public void should_return_an_error_when_conversation_has_only_one_participant() {
		// given
		User firstParticipant = new User(sequences.getNextUserId());
		users.add(firstParticipant);
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot add conversation, reason: not enough participants.");

		// when
		Conversation conversation = new Conversation(sequences.getNextConversationId());
		conversation.addParticipant(firstParticipant);
		conversations.add(conversation);
	}

}
