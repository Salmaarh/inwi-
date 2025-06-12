package ma.emsi.testautomation.repository;

import ma.emsi.testautomation.entity.TestExecution;

import java.util.List;
import java.util.Map;

public interface WebServiceExecutor {
    String execute(Map<String, Object> params);
    List<TestExecution> findAll();
    TestExecution save(TestExecution testExecution);
}
