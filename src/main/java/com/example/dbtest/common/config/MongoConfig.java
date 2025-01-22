package com.example.dbtest.common.config;

import com.mongodb.ReadPreference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class MongoConfig {
    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(uri);
    }

    @Bean
    public MongoOperations mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        log.info("uri = {}", uri);
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory);
        mongoTemplate.setReadPreference(ReadPreference.primary());
        return mongoTemplate;
    }

    @Bean
    public PlatformTransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTransactionManager(mongoDatabaseFactory);
    }

}
