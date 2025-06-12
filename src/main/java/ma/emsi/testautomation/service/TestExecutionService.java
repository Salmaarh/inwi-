package ma.emsi.testautomation.service;

import ma.emsi.testautomation.entity.TestExecution;
import ma.emsi.testautomation.repository.WebServiceExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TestExecutionService {
    private final WebServiceExecutor repository;

    public TestExecutionService(WebServiceExecutor Repository) {
        this.repository = Repository;
    }

    public TestExecution runTest(String testName) {
        TestExecution testExecution = new TestExecution();
        testExecution.setTestName(testName);
        testExecution.setStatus("SUCCESS");
        testExecution.setExecutionTime(LocalDateTime.now());
        return repository.save(testExecution);
    }

    public List<TestExecution> getAllTests() {
        return repository.findAll();
    }
}
