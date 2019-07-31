/*
package com.neo.rabbit.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "dead_letter_queue")
public class DeadLetterReceiver {

    @RabbitHandler
    public void process(String message) {
        System.out.println("Topic dead letter Receiver  : " + message);
    }

}
*/
