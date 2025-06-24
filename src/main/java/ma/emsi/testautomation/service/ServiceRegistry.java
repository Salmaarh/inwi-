package ma.emsi.testautomation.service;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class ServiceRegistry {

    private final Map<String, WebServiceExecutor> executors;

    public ServiceRegistry(Map<String, WebServiceExecutor> executors) {
        this.executors = executors;
    }

    public WebServiceExecutor getExecutor(String serviceName) {
        return executors.get(serviceName);
    }

    public boolean contains(String serviceName) {
        return executors.containsKey(serviceName);
    }
}
