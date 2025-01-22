package com.example.dbtest.domain.dao.wallet.impl;

import com.example.dbtest.domain.dao.wallet.CustomWalletRepository;
import com.example.dbtest.domain.entity.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomWalletRepositoryImpl implements CustomWalletRepository {
    private final MongoOperations mongoOperations;

    /**
     * 지갑의 잔고 차감
     * @param userId    유저 id
     * @param balance   차감할 잔고 액수
     */
    public void decBalance(Long userId, Long balance) {
        Query query = Query.query(new Criteria("userId").is(userId));
        Update update = new Update().inc("balance", -balance);
        mongoOperations.updateFirst(query, update, Wallet.class);
    }
}
