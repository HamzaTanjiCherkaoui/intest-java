package intest.domain.model;

public class Destination {

    private final DestinationType type;
    private final String name;

    public static Destination createT(String name) {
        return new Destination(DestinationType.TOPIC, name);
    }
    public static Destination createQ(String name) {
        return new Destination(DestinationType.QUEUE, name);
    }

    public Destination(DestinationType type, String name) {
        this.type = type;
        this.name = name;
    }

    public DestinationType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
