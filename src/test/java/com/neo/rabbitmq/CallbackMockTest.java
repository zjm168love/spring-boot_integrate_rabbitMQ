package com.neo.rabbitmq;

import com.neo.rabbit.topic.TopicConfirmCallback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @Auther: Pep
 * @Date: 2019-08-09
 * @Description:
 */
/*@RunWith(SpringRunner.class)
@SpringBootTest*/
@RunWith(MockitoJUnitRunner.class)
public class CallbackMockTest {

        @Mock
    private RabbitTemplate template;

   /* @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }*/

    @Test
    public void callBackTest() {
        doAnswer((Answer<Object>) invocation -> {
            TopicConfirmCallback confirmCallback = new TopicConfirmCallback();
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(UUID.randomUUID().toString());
            confirmCallback.confirm(correlationData, true, "aaa");
            return null;
        }).when(template).convertAndSend(anyString(), anyString(), any(Object.class), any(CorrelationData.class));

        this.template.convertAndSend("topic.limitedQueExchange", "topic.limitedMessage.first", new Object(), new CorrelationData());

    }

    @Test
    public void verifyTest(){

//        List<String> mockedList = mock(MyList.class);
        List<String> spyList=  spy(ArrayList.class);
        spyList.add("a");
        assertEquals(1, spyList.size());


        //        mockedList.size();
//        verify(mockedList).size();

    }

}
