package com.github.maximilientyc.conversations.domain.repositories;

import com.github.maximilientyc.conversations.commands.ConversationIntegrationTest;
import com.github.maximilientyc.conversations.domain.repositories.mongodb.MongoDbConversationRepository;
import com.github.maximilientyc.conversations.domain.repositories.mongodb.MongoDbMessageRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;
import org.junit.Before;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by @maximilientyc on 08/02/2016.
 */
public class MongoDbIntegrationTest extends ConversationIntegrationTest {

	public MongoDbIntegrationTest() throws IOException {
		super();
	}

	@Override
	public void prepareRepositories() throws Exception {
		super.prepareRepositories();
		MongoDatabase mongoDatabase = prepareMongoDatabase();
		super.conversationRepository = new MongoDbConversationRepository(mongoDatabase);
		super.messageRepository = new MongoDbMessageRepository(mongoDatabase);
	}

	@Before
	@Override
	public void initComponents() {
		super.initComponents();
	}

	public MongoDatabase prepareMongoDatabase() throws IOException {
		MongodForTestsFactory factory = MongodForTestsFactory.with(Version.Main.PRODUCTION);
		MongoClient mongo = factory.newMongo();
		return mongo.getDatabase("conversations-test-#" + UUID.randomUUID());
	}
}
