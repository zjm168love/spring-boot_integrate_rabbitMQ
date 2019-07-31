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
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        this.rabbitTemplate.convertAndSend("topicExchange", "topic1.message", context,correlationId);
        }
        */


    public void sendLimited(String context) {
        System.out.println("Sender : " + context);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        this.rabbitTemplate.convertAndSend("topic.limitedQueExchange", "topic.limitedMessage.first", context, correlationId);

//        this.rabbitTemplate.convertAndSend("topic.limitedQueExchange", "topic.limitedMessage.second", context, correlationId);
    }
}
