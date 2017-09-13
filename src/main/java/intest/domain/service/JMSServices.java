package intest.domain.service;

import intest.domain.model.Destination;
import intest.domain.model.UniversalMessagingServer;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import java.util.function.Consumer;

public class JMSServices {

    public static JMSConnectionContext connectToUMServer(UniversalMessagingServer um) {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.pcbsys.nirvana.nSpace.NirvanaContextFactory");
        env.put(Context.PROVIDER_URL, "nsp://" + um.getUrl() + ":" + um.getPort());

        try {
            Context ctx = new InitialContext(env);
            ConnectionFactory connFactory = (ConnectionFactory) ctx.lookup(um.getLookupName());
            return new JMSConnectionContext(ctx, connFactory.createConnection());
        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public static MessageConsumerContext createConsumer(JMSConnectionContext connCtx,
                                                        Destination destination,
                                                        Consumer<Message> msgConsumer) {
        try {
            Topic topic = (Topic) connCtx.getContext().lookup(destination.getName());

            Session session = connCtx.getConnection().createSession(false, Session.CLIENT_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(topic);

            consumer.setMessageListener(msgConsumer::accept);

            return new MessageConsumerContext(session, consumer);
        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        }
    }

}
