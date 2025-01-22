package com.example.dbtest.application.wallet;

import com.example.dbtest.domain.dao.item.ItemRepository;
import com.example.dbtest.domain.dao.wallet.WalletRepository;
import com.example.dbtest.domain.dao.wallet.WalletTransactionLogRepository;
import com.example.dbtest.domain.entity.Item;
import com.example.dbtest.domain.entity.Wallet;
import com.example.dbtest.domain.entity.WalletTransactionLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final WalletRepository walletRepository;
    private final ItemRepository itemRepository;
    private final WalletTransactionLogRepository walletTransactionLogRepository;

    /**
     * 아이템 구매
     * @param userId    구매한 유저 id
     * @param itemId    구매한 아이템 id
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void itemPurchase(Long userId, Long itemId) {
        Item item = itemRepository.findByItemId(itemId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템"));
        Wallet wallet = this.getBy(userId);
        wallet.decBalance(item.getPrice().longValue());
        walletRepository.decBalance(userId, item.getPrice().longValue());
    }

    /**
     * 지갑 조회
     * @param userId    유저 id
     * @return          조회 결과
     */
    public Wallet getBy(Long userId) {
        return walletRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지갑 정보"));
    }

    /**
     * 클라이언트에서 받은 트랜잭션 id로 된 데이터가 있는지 확인
     * @param txId  클라이언트에서 받은 트랜잭션 id
     * @return      중복 여부
     */
    public boolean isDuplicated(String txId) {
        return walletTransactionLogRepository.existsByTxId(txId);
    }

    /**
     * 클라이언트 id 데이터를 담고 있는 트랜잭션 로그 저장
     * @param txId  클라이언트 트랜잭션 id
     */
    public void saveWalletTransactionLog(String txId) {
        walletTransactionLogRepository.save(new WalletTransactionLog(txId));
    }
}
