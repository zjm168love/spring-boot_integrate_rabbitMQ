package com.neo.rabbit.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "topic.trade.firstTest", containerFactory = "tradeListenerContainerFactory")
public class MultiReceiver_trade {

    @RabbitHandler
    public void process(String message) {
        System.out.println(" trade consume  : " + message);
    }


}
