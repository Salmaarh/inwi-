package ma.emsi.testautomation.registry;

import ma.emsi.testautomation.entity.CreateSubscriber;
import ma.emsi.testautomation.entity.*;
import ma.emsi.testautomation.service.WebServiceExecutor;
import ma.emsi.testautomation.service.createsubscriberService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebServiceRegistry {

    private final Map<String, WebServiceExecutor> registry = new HashMap<>();

    public void register(String key, createsubscriberService executor) {
        registry.put(key, executor);
    }

    public WebServiceExecutor getWebService(String key) {
        return registry.get(key);
    }

    public boolean contains(String key) {
        return registry.containsKey(key);
    }

    public void registerWebService(String createSubscriber, CreateSubscriber createSubscriber1) {
    }

    public void registerWebService(String deleteSubscriber, DeleteSubscriber deleteSubscriber1) {
    }

    public void registerWebService(String adjustAccount, AdjustAccount AdjustAccount) {
    }

    public void registerWebService(String activeFirst, ActiveFirst ActiveFirst) {
    }
    public void registerWebService(String integrationEnq, IntegrationEnq IntegrationEnq) {
    }
}
