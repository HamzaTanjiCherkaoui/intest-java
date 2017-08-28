package intest.domain.model;

import java.util.Map;
import java.util.HashMap;

public class Environment {

    final Map<String, UniversalMessagingServer> ums = new HashMap<>();

    public void addUMServer(String key, UniversalMessagingServer um) {
        this.ums.put(key, um);
    }

    public Map<String, UniversalMessagingServer> getUms() {
        return ums;
    }
}
