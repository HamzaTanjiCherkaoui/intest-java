package intest.domain.model;

import java.util.Random;
import java.util.function.Consumer;

public class JMSProbe implements Probe {

    private final UniversalMessagingServer server;
    private final Destination destination;
    private final Consumer<String> handler;

    public JMSProbe(UniversalMessagingServer server, Destination destination, Consumer<String> handler) {
        this.server = server;
        this.destination = destination;
        this.handler = handler;
    }

    // jsute pour tests
    public void init() {
        Thread t = new Thread(() -> {
            Random rnd = new Random();
            for(int i = 0; i<rnd.nextInt(10); i++) {
                handleMessage("received message");
            }
        });
        t.run();
    }

    @Override
    public void listen() {
        System.out.println("Listening...");
    }

    @Override
    public void stopListen() {
        System.out.println("Stop listening");
    }

    @Override
    public void handleMessage(String message) {
        handler.accept(message);
    }
}
