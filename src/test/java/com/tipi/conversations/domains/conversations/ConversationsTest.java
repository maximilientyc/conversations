package com.tipi.conversations.domains.conversations;

import com.tipi.conversations.domains.conversations.repositories.InMemoryConversationsRepository;
import com.tipi.conversations.domains.users.User;
import com.tipi.conversations.domains.users.Users;
import com.tipi.conversations.domains.users.repositories.InMemoryUsersRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.repository.CrudRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Maximilien on 03/01/2016.
 */
public class ConversationsTest {

	private Users users;
	private CrudRepository usersRepository;
	private Conversations conversations;

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

	@Test
	public void should_contain_two_participants() {
		// given
		User firstParticipant = new User("0001");
		users.add(firstParticipant);
		User secondParticipant = new User("0002");
		users.add(secondParticipant);

		// when
		Conversation conversation = new Conversation("0001");
		conversation.addParticipant(firstParticipant);
		conversation.addParticipant(secondParticipant);
		conversations.add(conversation);

		// then
		Conversation storedConversation = conversations.getByConversationId("0001");
		assertThat(storedConversation.getParticipants()).hasSize(2);
	}

	@Test
	public void should_return_an_error_when_conversation_has_only_one_participant() {
		// given
		User firstParticipant = new User("0001");
		users.add(firstParticipant);
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Cannot add conversation, reason: not enough participants.");

		// when
		Conversation conversation = new Conversation("0001");
		conversation.addParticipant(firstParticipant);
		conversations.add(conversation);
	}

}
