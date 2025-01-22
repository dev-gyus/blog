package com.example.dbtest.domain.dao.wallet;

import com.example.dbtest.domain.entity.Wallet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WalletRepository extends MongoRepository<Wallet, ObjectId>, CustomWalletRepository {
    Optional<Wallet> findByUserId(Long userId);
}
