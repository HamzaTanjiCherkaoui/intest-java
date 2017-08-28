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

    @Override
    public String toString() {
        return "UniversalMessagingServer{" +
                "url='" + url + '\'' +
                ", port=" + port +
                '}';
    }
}
