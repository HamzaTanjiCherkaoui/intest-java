package intest.domain.model;

import intest.domain.service.JMSConnectionContext;
import intest.domain.service.JMSServices;
import intest.domain.service.MessageConsumerContext;
import org.junit.Test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class JMSServicesTest {

    private static final UniversalMessagingServer UM = new UniversalMessagingServer("165.227.154.145", 9000, "local_um");
    private static final Destination TOPIC = new Destination(DestinationType.TOPIC, "logistic/edi/response");


    private static void handleMessage(Message message) {
        if(message instanceof TextMessage) {
            try {
                System.out.println(((TextMessage) message).getText());
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Received message, but can't read it");
        }

    }

    @Test
    public void messageConsumerTest() throws InterruptedException {
        JMSConnectionContext connCtx = JMSServices.connectToUMServer(UM);
        MessageConsumerContext consumer = JMSServices.createConsumer(connCtx, TOPIC, JMSServicesTest::handleMessage);
        connCtx.startListeners();

        Thread.sleep(25000);
        consumer.stopConsumer();
        connCtx.stopConnection();
    }

}
