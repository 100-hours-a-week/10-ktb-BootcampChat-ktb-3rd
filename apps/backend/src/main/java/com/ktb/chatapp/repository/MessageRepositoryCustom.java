package com.ktb.chatapp.repository;

import java.util.List;

public interface MessageRepositoryCustom {
    List<String> findAllReadMessages(List<String> messageIds, int expectedReaders);
    void markAllRead(List<String> messageIds);
    List<String> markAllReadIfCompleted(List<String> messageIds, int expectedReaders);

}
