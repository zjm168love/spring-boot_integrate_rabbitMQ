package com.neo.rabbit.topic;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@Configuration
public class TopicRabbit_limitedQueueConfig {

    private static AtomicInteger successsAcks = new AtomicInteger(0);

    private static AtomicInteger failedAcks = new AtomicInteger(0);


    private final static String LIMITED_QUE_EXCHANGE = "topic.limitedQueExchange";

    private final static String LIMITED_MESSAGES_QUE_FIRST = "topic.limitedMessage.first";
    private final static String LIMITED_MESSAGES_QUE_SECOND = "topic.limitedMessage.second";

    private final static String TOPIC_LIMITED_ROUTE = "topic.limitedMessage.*";

    /**
     * message数量限制，并且绑定死信队列的队列
     *
     * @return
     */
    @Bean
    public Queue queueLengthLimited_first() {
        return new Queue(LIMITED_MESSAGES_QUE_FIRST, true, false, false, propertiesMap());
    }

    @Bean
    public Queue queueLengthLimited_second() {
        return new Queue(LIMITED_MESSAGES_QUE_SECOND, true, false, false, propertiesMap());
    }

    private Map<String, Object> propertiesMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-max-length", 5);
        map.put("x-overflow", "reject-publish");

//        map.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
//        map.put("x-dead-letter-routing-key", "normal");
        return map;
    }


    @Bean
    TopicExchange limitedQueExchange() {
        return new TopicExchange(LIMITED_QUE_EXCHANGE);
    }

    @Bean
    Binding bindingExchangeLimitedQue_first(Queue queueLengthLimited_first, TopicExchange limitedQueExchange) {
        return BindingBuilder.bind(queueLengthLimited_first).to(limitedQueExchange).with(TOPIC_LIMITED_ROUTE);
    }

    @Bean
    Binding bindingExchangeLimitedQue_second(Queue queueLengthLimited_second, TopicExchange limitedQueExchange) {
        return BindingBuilder.bind(queueLengthLimited_second).to(limitedQueExchange).with(TOPIC_LIMITED_ROUTE);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        template.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            System.out.println("回调id: " + correlationData.getId());
            if (ack) {
                successsAcks.incrementAndGet();
                System.out.println("successACK:" + successsAcks.get());
//                System.out.println("生产者回调-消息成功消费");
            } else {
                failedAcks.incrementAndGet();
                System.out.println("failedACK:" + failedAcks.get());
//                System.out.println("生产者回调-消息消费失败:" + cause);
            }
        });

        template.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("Returned message  " + message);
            System.out.println("Returned replyText  " + replyText);
        });

        template.setMandatory(true);
        return template;
    }

}
