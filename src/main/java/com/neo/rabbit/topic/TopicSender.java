package com.neo.rabbit.topic;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TopicSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;
/*
    public void send() {
        String context = "hi, i am message all";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("topicExchange", "topic.1", context);
    }

    public void send1() {
        String context = "hi, i am message 1";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("topicExchange", "topic.message", context);
    }

    public void send2() {
        String context = "hi, i am messages 2";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("topicExchange", "topic.messages", context);
    }*/

    public void sendLimited(String context) {
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("limitedQueExchange", "topic.limitedMessages", context);
    }


    public void send() {
        String context = "hi, i am message all";
        System.out.println("Sender : " + context);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        this.rabbitTemplate.convertAndSend("topicExchange", "topic.message", context,correlationId);
    }
}
