/*
package com.neo.rabbit.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "topic.limitedMessages")
public class LimitedQueReceiver {

    @RabbitHandler
    public void process(String message) {
        System.out.println("Topic limited messages Receiver  : " + message);
    }

}
*/
