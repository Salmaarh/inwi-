package ma.emsi.testautomation.service;

import ma.emsi.testautomation.entity.TestExecution;
import ma.emsi.testautomation.repository.TestExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TestExecutionService {

    private final TestExecutionRepository repository;

    @Autowired
    public TestExecutionService(TestExecutionRepository repository) {
        this.repository = repository;
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
