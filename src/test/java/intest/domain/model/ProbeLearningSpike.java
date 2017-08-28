package intest.domain.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProbeLearningSpike {

    @Test
    public void testProbe() {

    }

    @Test
    public void testMultipleProbes() throws InterruptedException {
        UniversalMessagingServer um = new UniversalMessagingServer("localhost", 9000);
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
