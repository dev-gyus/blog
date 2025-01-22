package com.example.dbtest.domain.dao.item;

import com.example.dbtest.domain.entity.Item;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ItemRepository extends MongoRepository<Item, ObjectId> {
    Optional<Item> findByItemId(Long itemId);
}
