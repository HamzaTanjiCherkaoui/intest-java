package intest.domain.model;

public class UniversalMessagingServer {
    private final String url;
    private final int port;
    private final String lookupName;

    public UniversalMessagingServer(String url, int port, String lookupName) {
        this.url = url;
        this.port = port;
        this.lookupName = lookupName;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

    public String getLookupName() {
        return lookupName;
    }

    @Override
    public String toString() {
        return "UniversalMessagingServer{" +
                "url='" + url + '\'' +
                ", port=" + port +
                '}';
    }
}
