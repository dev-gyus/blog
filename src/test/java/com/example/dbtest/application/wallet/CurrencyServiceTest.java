package com.example.dbtest.application.wallet;

import com.example.dbtest.domain.dao.item.ItemRepository;
import com.example.dbtest.domain.dao.wallet.WalletRepository;
import com.example.dbtest.domain.entity.Item;
import com.example.dbtest.domain.entity.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@TestPropertySource(properties = "spring.config.location = classpath:application-test.yml")
public class CurrencyServiceTest {
    @Autowired
    CurrencyService currencyService;
    @Autowired
    FacadeCurrencyService facadeCurrencyService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    WalletRepository walletRepository;


    List<Item> items =
        List.of(new Item(1L, 1000), new Item(2L, 2000), new Item(3L, 3000));
    Wallet wallet = new Wallet(1L, 1L, 50000L);

    @BeforeEach
    void prepare() {
        itemRepository.deleteAll();
        walletRepository.deleteAll();
        insertInitData();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @DisplayName("아이템 구매")
    @Test
    void purchaseItem() {
        // given
        System.out.println("구매 전 balance: " + wallet.getBalance());
        Item buyItem = items.get(0);
        // when
        currencyService.itemPurchase(wallet.getUserId(), buyItem.getItemId());
        // then
        Wallet foundWallet = currencyService.getBy(wallet.getUserId());
        System.out.println("구매 후 balance: " + foundWallet.getBalance());
        Assertions.assertEquals(wallet.getBalance() - buyItem.getPrice(), foundWallet.getBalance());
    }

    @DisplayName("아이템 병렬 10번 구매")
    @Test
    void repeatPurchaseItem() {
        // given
        System.out.println("구매 전 balance: " + wallet.getBalance());
        Item buyItem = items.get(0);
        int repeat = 10;
        String txId = UUID.randomUUID().toString();
        // when
        IntStream.range(0, repeat).parallel()
            .forEach(i -> {
                boolean retry = true;
                while (retry) {
                    try {
                        facadeCurrencyService.distributeLockedItemPurchase(txId, wallet.getUserId(), buyItem.getItemId());
                        retry = false;
                    } catch (Exception e) {
                        System.out.println("예외 발생. 재시도");
                        retry = true;
                    }
                }
            });
        // then
        Wallet foundWallet = currencyService.getBy(wallet.getUserId());
        System.out.println("구매 후 balance: " + foundWallet.getBalance());
        Assertions.assertEquals(wallet.getBalance() - buyItem.getPrice(), foundWallet.getBalance());

    }

    /**
     * 기초 데이터 생성
     */
    private void insertInitData() {
        itemRepository.saveAll(items);
        walletRepository.save(wallet);
    }
}