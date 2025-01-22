package com.example.dbtest.application.mongodb;

import com.example.dbtest.domain.entity.TestObj;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoDBInnerService {
    private final MongoOperations mongoOperations;

    @Transactional
    public void updateDocs(List<Integer> targetTestIds) {
        targetTestIds.parallelStream().forEach(id -> {
            Query query = Query.query(Criteria.where("test").is(id));
            Update update = new Update().inc("count", 1);
            mongoOperations.updateFirst(query, update, TestObj.class);
        });
    }

}
