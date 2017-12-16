import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jms.*;
import java.io.IOException;

public class ActiveMQ {
    //点对点的消息队列
    //这个也用不着记住  以后都是用spring来进行整合的
    @Test
    public void testMQProducer() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建生产者
        Queue queue = session.createQueue("test_queue");
        MessageProducer producer = session.createProducer(queue);
        TextMessage textMessage = session.createTextMessage("hello activeMQ,this is my try");
        producer.send(textMessage);
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testQConsumer() throws JMSException, IOException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("test_queue");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MessageListener() {
            //在这里面接收消息
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println(textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //队列中只要有消息被接受到 那个消息就会被删除
        //可以等待接收消息
        System.in.read();
        consumer.close();
        connection.close();
        session.close();
    }

    @Test
    public void testTopicPro() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建生产者
        Topic topic = session.createTopic("test topic");
        MessageProducer producer = session.createProducer(topic);
        TextMessage textMessage = session.createTextMessage("hello topic");
        producer.send(textMessage);
        producer.close();
        session.close();
        connection.close();
    }
    @Test
    public void testConsumer() throws IOException {
        //消费者 只需要在spring配置就好了
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        System.in.read();
    }
}
