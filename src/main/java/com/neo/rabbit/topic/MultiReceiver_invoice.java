package com.neo.rabbit.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "invoice.notify", containerFactory = "invoiceListenerContainerFactory")
public class MultiReceiver_invoice {

    @RabbitHandler
    public void process(String message) {
        System.out.println("___________________________invoice consume  : " + message);
    }


}
