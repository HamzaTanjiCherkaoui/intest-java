package intest.domain.service;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

public class MessageConsumerContext {

    private final Session session;
    private final MessageConsumer consumer;

    public MessageConsumerContext(Session session, MessageConsumer consumer) {
        this.session = session;
        this.consumer = consumer;
    }

    public Session getSession() {
        return session;
    }

    public MessageConsumer getConsumer() {
        return consumer;
    }

    public void stopConsumer() {
        try {
            if(consumer != null) {
                consumer.close();
            }
            if(session != null) {
                session.close();
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
