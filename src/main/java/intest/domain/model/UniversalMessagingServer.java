package intest.domain.model;

public class UniversalMessagingServer {
    private final String url;
    private final int port;

    public UniversalMessagingServer(String url, int port) {
        this.url = url;
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }
}
