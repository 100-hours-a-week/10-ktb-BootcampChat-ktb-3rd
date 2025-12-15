package com.ktb.chatapp.repository;

import com.ktb.chatapp.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryCustomImpl implements MessageRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<String> findAllReadMessages(
            List<String> messageIds,
            int expectedReaders
    ) {
        Query query = new Query(
                Criteria.where("_id").in(messageIds)
                        .and("allRead").ne(true)
                        .and("readerCount").is(expectedReaders)
        );
        query.fields().include("_id");

        return mongoTemplate.findDistinct(
                query,
                "_id",
                Message.class,
                String.class
        );
    }

    @Override
    public void markAllRead(List<String> messageIds) {
        Query query = Query.query(
                Criteria.where("_id").in(messageIds)
        );

        Update update = new Update()
                .set("allRead", true);

        mongoTemplate.updateMulti(query, update, Message.class);
    }

    @Override
    public List<String> markAllReadIfCompleted(
            List<String> messageIds,
            int expectedReaders
    ) {
        Query query = Query.query(
                Criteria.where("_id").in(messageIds)
                        .and("readerCount").is(expectedReaders)
                        .and("allRead").ne(true)
        );

        Update update = new Update().set("allRead", true);
        mongoTemplate.updateMulti(query, update, Message.class);

        return mongoTemplate.findDistinct(
                query,
                "_id",
                Message.class,
                String.class
        );
    }
}