package intest.domain.model;

import org.junit.Test;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class ProbeLearningSpike {

    @Test
    public void testProbe() throws JMSException, InterruptedException {
        UniversalMessagingServer um = new UniversalMessagingServer("165.227.154.145", 9000, "local_um");
        Destination topic = new Destination(DestinationType.TOPIC, "logistic/edi/response");
        Connection conn = listenToDestination(um, topic, System.out::println);
        conn.start();

        Thread.sleep(25000);
    }

    Connection listenToDestination(UniversalMessagingServer um,
                             Destination destination,
                             Consumer<String> onReceivedMessage) {
        try {

            // PARTIE 1 : CONNECTION => UM + properties
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.pcbsys.nirvana.nSpace.NirvanaContextFactory");
            env.put(Context.PROVIDER_URL, "nsp://" + um.getUrl() + ":" + um.getPort());

            Context ctx = new InitialContext(env);
            ConnectionFactory connFactory = (ConnectionFactory) ctx.lookup(um.getLookupName());
            Connection conn = connFactory.createConnection();

            // PARTIE 2 : setup du listener
            Topic topic = (Topic) ctx.lookup(destination.getName());

            Session session = conn.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(topic);

            consumer.setMessageListener(message -> {
                if(message instanceof TextMessage) {
                    try {
                        onReceivedMessage.accept(((TextMessage) message).getText());
                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Received message, but can't read it");
                }
            });

            return conn;
        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        }
    }

    //@Test
    public void testMultipleProbes() throws InterruptedException {
        UniversalMessagingServer um = new UniversalMessagingServer("localhost", 9000, "local_um");
        Destination t1 = new Destination(DestinationType.TOPIC, "/logistic/test");
        Destination t2 = new Destination(DestinationType.TOPIC, "/logistic/test2");

        List<String> receivedMessage = new CopyOnWriteArrayList<>();

        Probe p1 = new JMSProbe(um, t1, message -> receivedMessage.add("probe 1 " + message));
        Probe p2 = new JMSProbe(um, t2, message -> receivedMessage.add("probe 2 " + message));

        List<Probe> probes = Arrays.asList(p1, p2);

        probes.forEach(Probe::listen);
        probes.forEach(Probe::init);
        Thread.sleep(3000);
        probes.forEach(Probe::stopListen);

        receivedMessage.forEach(System.out::println);
    }

}
