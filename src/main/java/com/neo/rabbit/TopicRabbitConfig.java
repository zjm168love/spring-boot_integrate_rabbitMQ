package com.neo.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@Configuration
public class TopicRabbitConfig {

    private static AtomicInteger successsAcks = new AtomicInteger(0);

    private static AtomicInteger failedAcks = new AtomicInteger(0);


    private final static String message = "topic.message";
    private final static String messages = "topic.messages";

    private final static String LIMITED_MESSAGES_QUE = "topic.limitedMessages";
    private final static String DEAD_LETTER_EXCHANGE = "dead_letter_exchange";
    private final static String DEAD_LETTER_QUEUE = "dead_letter_queue";


    /**
     * message数量限制，并且绑定死信队列的队列
     *
     * @return
     */
    /*@Bean
    public Queue queueLengthLimited() {
        return new Queue(LIMITED_MESSAGES_QUE, false, false, false, propertiesMap());
    }

    private Map<String, Object> propertiesMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-max-length", 5);
        map.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
//        map.put("x-dead-letter-routing-key", "normal");
        return map;
    }


    @Bean
    TopicExchange limitedQueExchange() {
        return new TopicExchange("limitedQueExchange");
    }


    @Bean
    Binding bindingExchangeLimitedQue(Queue queueLengthLimited, TopicExchange limitedQueExchange) {
        return BindingBuilder.bind(queueLengthLimited).to(limitedQueExchange).with(LIMITED_MESSAGES_QUE);
    }*/


    /**
     * 死信队列配置
     *
     * @return
     */
    /*@Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE, true);
    }

    @Bean("deadLetterExchange")
    public FanoutExchange deadLetterExchange() {
        return new FanoutExchange(DEAD_LETTER_EXCHANGE);
    }

    @Bean
    Binding bindingExchangeDeadLetterQue(Queue deadLetterQueue, @Qualifier("deadLetterExchange") FanoutExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange);
    }*/
    @Bean
    public Queue queueMessage() {
        return new Queue(TopicRabbitConfig.message);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }


    /*@Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        container.
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }*/

    /*@Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses(this.props.getAddresses());
        factory.setUsername(this.props.getUsername());
        factory.setPassword(this.props.getPassword());
        factory.setVirtualHost(this.props.getVirtualHost());
        factory.setPublisherConfirms(true);
        factory.setPublisherReturns(true);

        return factory;
    }*/

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

    /*

     @Bean
    public Queue queueMessage() {
        return new Queue(TopicRabbitConfig.message);
    }

    @Bean
    public Queue queueMessages() {
        return new Queue(TopicRabbitConfig.messages);
    }



    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

    @Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
    }*/
}
