package intest.domain.model;

public interface Probe {
    void listen();

    // todo: remove
    void init();

    void stopListen();

    void handleMessage(String message);
}
