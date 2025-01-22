package com.example.dbtest.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document("walletTransactionLog")
public class WalletTransactionLog {
    @Id
    private ObjectId id;
    private String txId;

    public WalletTransactionLog(String txId) {
        this.txId = txId;
    }
}
