package com.neo.rabbit.topic;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TopicSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;
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
        try {
            this.rabbitTemplate.convertAndSend("topic.limitedQueExchange", "topic.limitedMessage.first", context, correlationId);
            /*this.rabbitTemplate.convertAndSend("topic.limitedQueExchange", "topic.limitedMessage.first", context, m -> {
                m.getMessageProperties().setCorrelationId(UUID.randomUUID().toString());
                return m;
            });*/
        } catch (Exception e) {
            System.out.println(e);
        }

    }


    public void sendOperation() {
        List<String> messages = new ArrayList<>(2000);

        for (int i = 0; i < 20000; i++) {
            messages.add("operation MEssages:" + i);
        }

        Boolean result = this.rabbitTemplate.invoke(t -> {
            messages.forEach(m -> {
                CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
                System.out.println("send message:" + m);
                t.convertAndSend("topic.limitedQueExchange", "topic.limitedMessage.first", m, correlationId);
            });

            boolean confirmResult = t.waitForConfirms(10000);
            System.out.println("=======================================");
            System.out.println("confirmResult:" + confirmResult);
            System.out.println("after confirms");
            return true;
        });

        System.out.println("operation result:" + result);
    }

}
