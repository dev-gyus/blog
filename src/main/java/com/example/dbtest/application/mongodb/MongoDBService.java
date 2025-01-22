package com.example.dbtest.application.mongodb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoDBService {
    private final MongoDBInnerService mongoDBInnerService;

    @Transactional
    public void updateDocs(List<Integer> targetTestIds) {
        mongoDBInnerService.updateDocs(targetTestIds);
    }

}
