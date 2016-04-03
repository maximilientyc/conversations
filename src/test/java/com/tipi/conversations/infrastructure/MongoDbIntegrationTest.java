package com.tipi.conversations.infrastructure;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.tipi.conversations.commands.ConversationIntegrationTest;
import com.tipi.conversations.infrastructure.mongodb.MongoDbConversationRepository;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by @maximilientyc on 08/02/2016.
 */
public class MongoDbIntegrationTest extends ConversationIntegrationTest {

	public MongoDbIntegrationTest() throws IOException {
		super();

		MongoDatabase mongoDatabase = prepareMongoDatabase();
		super.conversationRepository = new MongoDbConversationRepository(mongoDatabase);
	}

	public MongoDatabase prepareMongoDatabase() throws IOException {
		MongodForTestsFactory factory = MongodForTestsFactory.with(Version.Main.PRODUCTION);
		MongoClient mongo = factory.newMongo();
		return mongo.getDatabase("conversations-test-#" + UUID.randomUUID());
	}

}
