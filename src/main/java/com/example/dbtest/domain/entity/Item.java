package com.example.dbtest.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document("item")
public class Item {
    @Id
    private ObjectId id;
    private Long itemId;
    private Integer price;

    public Item(Long itemId, Integer price) {
        this.itemId = itemId;
        this.price = price;
    }
}
