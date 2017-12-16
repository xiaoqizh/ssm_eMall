package cn.e3mall.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;

public class TestMQ {
    //测试生产者
    @Test
    public void test1() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        //spring简化到 使用jmsTemplate 就行了   因为activemq 符合jms规范
        ActiveMQQueue queue = applicationContext.getBean(ActiveMQQueue.class);
        //send 方法就表明了是一个生产者
        jmsTemplate.send(queue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
              TextMessage message = session.createTextMessage("wo ai ni bin binaaa");
                return message;
            }
        });
    }

    @Test
    public void testConsumer() throws IOException {
        //消费者 只需要在spring配置就好了
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        System.in.read();
    }
}
