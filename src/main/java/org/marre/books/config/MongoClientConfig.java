package org.marre.books.config;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableMongoRepositories(basePackages = "org.marre.books.repository")
@PropertySource("classpath:application.properties")
public class MongoClientConfig extends AbstractMongoConfiguration {
    private final MongoServer mongoServer;

    @Autowired
    public MongoClientConfig(MongoServer mongoServer) {
        this.mongoServer = mongoServer;
    }

    @Override
    protected String getDatabaseName() {
        return "demo-test";
    }

    @Bean(destroyMethod="close")
    @Override
    public MongoClient mongoClient() {
        var serverAddress = mongoServer.getLocalAddress();

        return new MongoClient(new ServerAddress(serverAddress));
    }

    @Override
    protected Collection<String> getMappingBasePackages() {
        return Collections.singleton("org.marre.books.domain");
    }
}
