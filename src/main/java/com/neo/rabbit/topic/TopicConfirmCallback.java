package com.neo.rabbit.topic;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: Pep
 * @Date: 2019-08-11
 * @Description:
 */
@Component
public class TopicConfirmCallback implements RabbitTemplate.ConfirmCallback {
    private static AtomicInteger successsAcks = new AtomicInteger(0);

    private static AtomicInteger failedAcks = new AtomicInteger(0);

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, java.lang.String cause) {
        System.out.println("回调id: " + correlationData.getId());
        if (ack) {
            successsAcks.incrementAndGet();
            System.out.println("successACK:" + successsAcks.get());
//                System.out.println("生产者回调-消息成功消费");
        } else {
            failedAcks.incrementAndGet();
            System.out.println("failedACK:" + failedAcks.get());

            System.out.println("correlationData:" + correlationData);
//                System.out.println("生产者回调-消息消费失败:" + cause);
        }
    }

}
