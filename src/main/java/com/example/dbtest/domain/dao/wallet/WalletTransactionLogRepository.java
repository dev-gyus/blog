package com.example.dbtest.domain.dao.wallet;

import com.example.dbtest.domain.entity.WalletTransactionLog;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WalletTransactionLogRepository extends MongoRepository<WalletTransactionLog, ObjectId> {
    boolean existsByTxId(String txId);
}
