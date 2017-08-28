package intest.domain.model;

import org.junit.Test;

public class EnvironmentLearningSpike {

    @Test
    public void testCreateServer() {
        Environment env = new Environment();
        env.addUMServer("um1", new UniversalMessagingServer("localhost", 9000));

        env.getUms().forEach((key, um) -> System.out.println("UM server called " + key + " " + um));
    }

}
