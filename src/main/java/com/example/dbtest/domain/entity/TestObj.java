package com.example.dbtest.domain.entity;

import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "test")
public class TestObj {
    @Id
    private ObjectId id;
    private Integer test;

    protected TestObj() {}

    public TestObj(Integer test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "TestObj{" +
            "id=" + id +
            ", test=" + test +
            '}';
    }
}
