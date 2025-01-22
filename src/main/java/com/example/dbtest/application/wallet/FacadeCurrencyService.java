
package com.example.dbtest.application.wallet;

import com.example.dbtest.domain.dao.item.ItemRepository;
import com.example.dbtest.domain.dao.wallet.WalletRepository;
import com.example.dbtest.domain.entity.Item;
import com.example.dbtest.domain.entity.Wallet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class FacadeCurrencyService {
    private final CurrencyService currencyService;
    private final RedissonClient redisson;

    /**
     * 아이템 구매
     * @param userId    구매한 유저 id
     * @param itemId    구매한 아이템 id
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void distributeLockedItemPurchase(String txId, Long userId, Long itemId) {
        RLock lock = null;
        String key = "currency-" + userId;
        try {
            lock = redisson.getLock(key);
            // lock 획득 시도 1초 대기, 3초후 강제 lock release
            if (lock.tryLock(1000, 3000, TimeUnit.MILLISECONDS)) {
                // 클라이언트에서 받은 트랜잭션 id가 이미 이전에 lock 획득을 해서 프로세스를 진행 했는지 여부
                if(currencyService.isDuplicated(txId)) {
                    log.info("중복된 txId:{}", txId);
                    return;
                }
                // 처음 받은 txId라면 거래 로그를 추가합니다
                currencyService.saveWalletTransactionLog(txId);
                // 아이템을 구매합니다.
                currencyService.itemPurchase(userId, itemId);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
