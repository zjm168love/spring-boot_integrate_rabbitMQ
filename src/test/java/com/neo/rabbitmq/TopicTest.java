package com.neo.rabbitmq;

import com.neo.rabbit.topic.TopicSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicTest {

    @Autowired
    private TopicSender sender;

   /* @Test
    public void topic() throws Exception {
        sender.send();
    }

    @Test
    public void topic1() throws Exception {
        sender.send1();
    }

    @Test
    public void topic2() throws Exception {
        sender.send2();
    }

    @Test
    public void topic_MQShutdown_exception_Test() {

        try {
            sender.send();

        } catch (AmqpIOException e) {
            e.getCause();
        }
    }*/

   /*
    @Test
    public void topic() throws Exception {
        *//*for (int i = 1; i < 50000; i++) {
            sender.send();
        }*//*
        sender.send();

    }*/


    @Test
    public void topic_MQLimitedQue_test() throws InterruptedException {
       /* for (int i = 1; i < 40000; i++) {
            String context = "hi, this is limited que message:" + i;
            sender.sendLimited(context);

        }
        Thread.sleep(20000L);*/
        String context = "hi, this is limited que message:";
        sender.sendLimited(context);

    }


    @Test
    public void topic_opration_test() throws InterruptedException {
        sender.sendOperation();
        Thread.sleep(20000L);
    }


}
