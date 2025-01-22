package com.example.dbtest.domain.dao.wallet;

public interface CustomWalletRepository {
    void decBalance(Long userId, Long balance);
}
