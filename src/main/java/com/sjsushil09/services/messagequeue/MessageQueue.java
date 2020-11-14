package com.sjsushil09.services.messagequeue;

public interface MessageQueue {
    void sendMessage(String topic,MQMessage message);
    MQMessage consumeMessage(String topic);
}
