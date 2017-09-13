package intest.learningSpike;

import intest.domain.model.Destination;
import intest.domain.model.UniversalMessagingServer;
import intest.domain.service.JMSConnectionContext;
import intest.domain.service.JMSServices;
import intest.domain.service.MessageConsumerContext;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class JMSServicesTests {

    private static final UniversalMessagingServer UM =
            new UniversalMessagingServer("165.227.154.145", 9000, "local_um");

    private static final List<Destination> DESTS =
            Arrays.asList(Destination.createT("a"),
                    Destination.createT("b"),
                    Destination.createT("c"),
                    Destination.createT("d"));

    private static Map<String, List<Message>> receivedMessages = new ConcurrentHashMap<>();

    // test consumer, do not use in prod
    // not concurrent
    private static Consumer<Message> notify(String topic) {
        return m -> {
            List<Message> currentMessages = receivedMessages.getOrDefault(topic, new CopyOnWriteArrayList<>());
            currentMessages.add(m);
            receivedMessages.put(topic, currentMessages);
        };
    }

    public static void main(String[] args) throws InterruptedException {
        JMSConnectionContext connCtx = JMSServices.connectToUMServer(UM);
        List<MessageConsumerContext> consumers =
                DESTS.stream()
                        .map(dest -> JMSServices.createConsumer(connCtx, dest, notify(dest.getName())))
                        .collect(Collectors.toList());

        connCtx.startListeners();

        Thread.sleep(25000);

        consumers.forEach(MessageConsumerContext::stopConsumer);
        connCtx.stopConnection();

        DESTS.forEach(dest -> {
            System.out.println("Message received for topic : " + dest.getName());
            receivedMessages.getOrDefault(dest.getName(), Collections.emptyList()).stream()
                    .map(msg -> {
                        try {
                            if(msg instanceof TextMessage) {
                                return ((TextMessage) msg).getText();
                            } else {
                                return "Unreadable message";
                            }
                        } catch(JMSException e) {
                            throw new RuntimeException(e);
                        }
                    }).forEach(System.out::println);
        });
    }
}
