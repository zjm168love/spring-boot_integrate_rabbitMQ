package com.neo.rabbit.topic;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.connection.SimpleResourceHolder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Auther: Pep
 * @Date: 2019-08-15
 * @Description:
 */
@Component
public class MultiRoutingSender {
    @Autowired
    private RabbitTemplate routingTemplate;


    public void sendLimited_invoice(String context) {

        /*try {
            SimpleResourceHolder.bind(routingTemplate.getConnectionFactory(), "invoice");
//            SimpleResourceHolder.getResources().put(routingTemplate.getConnectionFactory(), "invoice");
//            SimpleResourceHolder.getResources().put(routingTemplate.getConnectionFactory(), "invoice");
//            SimpleResourceHolder.bind(routingTemplate.getConnectionFactory(), "trade");
            System.out.println("Sender : " + context);
            CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
            this.routingTemplate.convertAndSend("topic.limitedQueExchange",
                    "invoice.notify", context, m -> {
                        m.getMessageProperties().setHeader("cfKey", "invoice");
                        return m;
                    }, correlationId);
        } finally {
            SimpleResourceHolder.unbind(routingTemplate.getConnectionFactory());
        }
*/


        System.out.println("Sender : " + context);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        try {
            this.routingTemplate.convertAndSend("topic.limitedQueExchange",
                    "invoice.notify", context, m -> {
                        m.getMessageProperties().setHeader("cfKey", "invoice");
                        return m;
                    }, correlationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendLimited_trade(String context) {
        System.out.println("Sender : " + context);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        try {
            this.routingTemplate.convertAndSend("trade_test",
                    "topic.trade.firstTest", context, m -> {
                        m.getMessageProperties().setHeader("cfKey", "trade");
                        return m;
                    }, correlationId);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
