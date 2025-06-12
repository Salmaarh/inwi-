package ma.emsi.testautomation.service;

import ma.emsi.testautomation.entity.TestExecution;
import org.springframework.stereotype.Component;
import ma.emsi.testautomation.repository.WebServiceExecutor;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component("ActiveFirst")
public class ActiveFirstService implements WebServiceExecutor {

    @Override
    public String execute(Map<String, Object> params) {
        return "✅ ActiveFirst exécuté avec succès";
    }

    @Override
    public List<TestExecution> findAll() {
        return Collections.emptyList();
    }

    @Override
    public TestExecution save(TestExecution testExecution) {
        return null;
    }
}
