package com.example.dbtest.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document("wallet")
public class Wallet {
    @Id
    private ObjectId id;
    private Long walletId;
    private Long userId;
    private Long balance;

    public Wallet(Long walletId, Long userId, Long balance) {
        this.walletId = walletId;
        this.userId = userId;
        this.balance = balance;
    }

    public void incBalance(Long balance) {
        this.balance += balance;
    }

    public void decBalance(Long balance) {
        if (this.balance < balance) {
            throw new IllegalArgumentException("잔액 부족");
        }
        this.balance -= balance;
    }
}
