package com.example.dbtest;

import com.example.dbtest.domain.entity.TestObj;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
public class DbTestObj {
    @Autowired
    MongoOperations mongoOperations;

    @DisplayName("insert 10000건 반복")
    @Test
    @Rollback
    void test() {
        long startTime = System.currentTimeMillis();
        insertLoop(10_000);
        long endTime = System.currentTimeMillis();
        System.out.println("처리시간 : " + (endTime - startTime) + "ms");
    }

    // 매개변수로 주어진 times 값에 따라 데이터 반복 삽입
    private void insertLoop(int times) {
        IntStream.range(0, times).forEach(i -> {
            mongoOperations.save(new TestObj(i));
        });
    }

    @DisplayName("data 10000건 조회")
    @Test
    @Rollback
    void test2() {
        prepareDataList(10_000);
        long startTime = System.currentTimeMillis();
        readData(10_000);
        long endTime = System.currentTimeMillis();
        System.out.println("처리시간 : " + (endTime - startTime) + "ms");
    }

    // 매개변수로 주어진 times 값에 따라 데이터 반복 조회
    private void readData(int times) {
        IntStream.range(0, times).forEach(i -> {
           mongoOperations.find(Query.query(Criteria.where("test").is(i)), TestObj.class);
        });
    }

    // 매개변수로 주어진 count 값에 따라 사전 데이터 저장
    private void prepareDataList(int count) {
        long startTime = System.currentTimeMillis();
        List<TestObj> result = new ArrayList<>();
        IntStream.range(0, count).forEach(i -> {
            result.add(new TestObj(i));
        });
        mongoOperations
            .bulkOps(BulkOperations.BulkMode.UNORDERED, TestObj.class)
            .insert(result)
                .execute();
        long endTime = System.currentTimeMillis();
        System.out.println("data insert시간 : " + (endTime - startTime) + "ms");
    }

}
