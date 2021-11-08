package ru.tinkoff.qa.neptune.spring.data.model;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

public class QTestEntity extends EntityPathBase<TestEntity> {

    public static final QTestEntity qTestEntity = new QTestEntity(TestEntity.class, "testEntity");
    public final NumberPath<Long> id = createNumber("id", Long.class);
    public final StringPath name = createString("name");
    public final CollectionPath<String, StringPath> listData = new CollectionPath<>(String.class, StringPath.class, "p") {

    };
    public final ArrayPath<String, StringPath> arrayData = createArray("arrayData", String.class);

    public QTestEntity(Class<? extends TestEntity> type, String variable) {
        super(type, variable);
    }

    public QTestEntity(Class<? extends TestEntity> type, PathMetadata metadata) {
        super(type, metadata);
    }

    public QTestEntity(Class<? extends TestEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
    }
}
