package com.example.dbtest.application;

import com.example.dbtest.application.mongodb.MongoDBService;
import com.example.dbtest.domain.entity.TestObj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
public class MongoDBTransactionTest {
    @Autowired
    MongoDBService mongoDBTransaction;

    @Autowired
    MongoOperations mongoOperations;

    @BeforeEach
    void prepare() {
        mongoOperations.dropCollection(TestObj.class);
    }

    @DisplayName("트랜잭션 Abort -> Retry 테스트")
    @Test
    void abortRetry() {
        List<Integer> testIds = insertDocs().stream().map(TestObj::getTest).toList();
        IntStream.range(0, 100).parallel().forEach(i -> {
            try {
                mongoDBTransaction.updateDocs(testIds);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                boolean doUpdate = true;
                while(doUpdate) {
                    try {
                        mongoDBTransaction.updateDocs(testIds);
                        doUpdate = false;
                    } catch (Exception e2) {
                    }
                }
            }
        });
    }

    private List<TestObj> insertDocs() {
        List<TestObj> testObjs = List.of(new TestObj(1), new TestObj(2));
        mongoOperations.insertAll(testObjs);
        return testObjs;
    }

}