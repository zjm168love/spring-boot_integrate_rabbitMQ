package com.neo.rabbit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo.rabbit.topic.TopicConfirmCallback;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class MultiConnectionFactoryConfig {

    @Autowired
    private TopicConfirmCallback topicConfirmCallback;


    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private Integer port = 5672;


    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host.invoice:invoice}")
    private String vHostInvoice;

    @Value("${spring.rabbitmq.virtual-host.trade:trade}")
    private String vHostTrade;


    private final static String LOOK_UP_KEY = "messageProperties.headers['cfKey']";


    // TODO: 2019-08-15
    private final static String LIMITED_QUE_EXCHANGE = "topic.limitedQueExchange";
    private final static String TOPIC_LIMITED_ROUTE = "topic.limitedMessage.first";
    private final static String LIMITED_MESSAGES_QUE_FIRST = "topic.limitedMessage.first";

    private final static String TRADE_TEST = "trade_test";


    @Bean
    TopicExchange limitedQueExchange() {
        return new TopicExchange(LIMITED_QUE_EXCHANGE);
    }

    @Bean
    public Queue queueLengthLimited_first() {
        return new Queue(LIMITED_MESSAGES_QUE_FIRST, true);
    }

    @Bean
    Binding bindingExchangeLimitedQue_first(Queue queueLengthLimited_first, TopicExchange limitedQueExchange) {
        return BindingBuilder.bind(queueLengthLimited_first).to(limitedQueExchange).with(TOPIC_LIMITED_ROUTE);
    }

    //    @Bean("connectionFactoryInvoice")
    public ConnectionFactory connectionFactoryInvoice() {
        CachingConnectionFactory connectionFactoryInvoice = new CachingConnectionFactory("localhost");
        connectionFactoryInvoice.setVirtualHost(vHostInvoice);
        return connectionFactoryInvoice;
    }

    /*@Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactoryInvoice = new CachingConnectionFactory("localhost");
        return connectionFactoryInvoice;
    }*/

    //    @Bean("_connectionFactoryTrade")
    private ConnectionFactory _connectionFactoryTrade() {
        CachingConnectionFactory connectionFactoryInvoice = new CachingConnectionFactory("localhost");
        connectionFactoryInvoice.setVirtualHost(vHostTrade);
        return connectionFactoryInvoice;
    }


    //    @Bean("_simpleRoutingConnectionFactory")
    private ConnectionFactory _simpleRoutingConnectionFactory(ConnectionFactory connectionFactory) {
        Map<Object, ConnectionFactory> factoryHashMap = new HashMap<>();
        factoryHashMap.put(vHostInvoice, connectionFactoryInvoice());
        factoryHashMap.put(vHostTrade, _connectionFactoryTrade());

        SimpleRoutingConnectionFactory simpleRoutingConnectionFactory = new SimpleRoutingConnectionFactory();
        simpleRoutingConnectionFactory.setTargetConnectionFactories(factoryHashMap);

        simpleRoutingConnectionFactory.setDefaultTargetConnectionFactory(connectionFactory);
        // TODO: 2019-08-15
        return simpleRoutingConnectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(_simpleRoutingConnectionFactory(connectionFactory));

        Expression expression = new SpelExpressionParser()
                .parseExpression(LOOK_UP_KEY);
        template.setSendConnectionFactorySelectorExpression(expression);
        template.setMessageConverter(fastJson2Converter());

        template.setConfirmCallback(topicConfirmCallback);
        template.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("Returned message  " + message);
            System.out.println("Returned replyText  " + replyText);
            message.getMessageProperties().getCorrelationId();
            message.getMessageProperties().getHeaders().get("spring_returned_message_correlation").toString();
        });

        template.setMandatory(true);
        return template;
    }


    //consumer listener
    @Bean("invoiceListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory invoiceListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactoryInvoice());
        factory.setMessageConverter(fastJson2Converter());
        return factory;
    }

    @Bean("tradeListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory tradeListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(_connectionFactoryTrade());
        factory.setMessageConverter(fastJson2Converter());
        return factory;
    }

//    @Bean
    public Jackson2JsonMessageConverter fastJson2Converter() {
        return new Jackson2JsonMessageConverter();
    }

}
