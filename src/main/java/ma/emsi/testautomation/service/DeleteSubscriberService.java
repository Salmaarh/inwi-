package ma.emsi.testautomation.service;

import ma.emsi.testautomation.entity.TestExecution;
import org.springframework.stereotype.Component;
import ma.emsi.testautomation.repository.WebServiceExecutor;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component("DeleteSubscriber")
public class DeleteSubscriberService implements WebServiceExecutor {
    @Override
    public String execute(Map<String, Object> params) {
        return "✅ DeleteSubscriber exécuté avec succès";
    }
    public List<TestExecution> findAll() { return List.of(); }
    public TestExecution save(TestExecution t) { return null; }
}
