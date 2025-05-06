package ma.emsi.testautomation.service;

import jakarta.transaction.Transactional;
import ma.emsi.testautomation.exception.ResourceNotFoundException;
import ma.emsi.testautomation.model.TestEntity;
import ma.emsi.testautomation.model.TestResult;
import ma.emsi.testautomation.model.TestRequest;
import ma.emsi.testautomation.model.TestReport;
import ma.emsi.testautomation.repository.TestRepository;
import ma.emsi.testautomation.repository.TestReportRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import groovy.lang.GroovyShell;
import groovy.lang.Binding;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional
public class TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    private final TestRepository testRepository;
    private final TestReportRepository testReportRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    public TestService(TestRepository testRepository, TestReportRepository testReportRepository) {
        this.testRepository = testRepository;
        this.testReportRepository = testReportRepository;
    }

    public TestResult executeGroovyScriptTest(TestRequest testRequest) {
        logger.info("Exécution d’un test Groovy : {}", testRequest.getTestName());

        TestEntity test = new TestEntity();
        test.setStatus("RUNNING");
        test.setLogs("Début d'exécution du script Groovy...");
        testRepository.save(test);

        TestReport report = new TestReport();
        report.setTestName("Groovy Test - " + testRequest.getTestName());
        report.setStartTime(LocalDateTime.now());

        long start = System.currentTimeMillis();

        try {
            Binding binding = new Binding();
            GroovyShell shell = new GroovyShell(binding);

            Object result = shell.evaluate(
                    "try {\n" +
                            testRequest.getScript() + "\n" +
                            "} catch(Exception e) {\n" +
                            "   println 'Erreur : ' + e.message\n" +
                            "}"
            );

            long duration = System.currentTimeMillis() - start;
            test.setStatus("SUCCESS");
            test.setLogs(test.getLogs() + "\nRésultat : " + result + "\nDurée : " + duration + " ms");
            testRepository.save(test);

            report.setStatus("PASSED");
            report.setErrorMessage(null);
            report.setEndTime(LocalDateTime.now());
            testReportRepository.save(report);

            return new TestResult(test.getId(), "SUCCESS", test.getLogs(), duration);

        } catch (Exception e) {
            return handleTestFailure(test, report, e);
        }
    }

    public TestResult executeTestAsync(TestRequest testRequest) {
        logger.info("Soumission du test à la file d'attente : {}", testRequest.getTestName());

        TestEntity test = new TestEntity();
        test.setStatus("QUEUED");
        test.setLogs("Test en attente dans la file...");
        testRepository.save(test);

        executorService.submit(() -> {
            try {
                executeGroovyScriptTest(testRequest); // Gère lui-même rapport & logs
            } catch (Exception e) {
                logger.error("Erreur pendant l’exécution async : {}", e.getMessage());
            }
        });

        return new TestResult(test.getId(), "QUEUED", test.getLogs(), 0);
    }

    public TestResult executeTest(TestRequest testRequest) {
        logger.info("Début de l'exécution du test : {}", testRequest.getTestName());

        TestEntity test = new TestEntity();
        test.setStatus("RUNNING");
        test.setLogs("Début de l'exécution du test : " + testRequest.getTestName());
        testRepository.save(test);

        TestReport report = new TestReport();
        report.setTestName("Standard Test - " + testRequest.getTestName());
        report.setStartTime(LocalDateTime.now());

        logger.info("Test {} en cours d'exécution avec ID {}", testRequest.getTestName(), test.getId());

        try {
            // Ici, tu peux mettre un vrai test, ou simuler un comportement
            Thread.sleep(1000);

            test.setStatus("SUCCESS");
            test.setLogs(test.getLogs() + "\nTest standard terminé avec succès.");
            testRepository.save(test);

            report.setStatus("PASSED");
            report.setEndTime(LocalDateTime.now());
            report.setErrorMessage(null);
            testReportRepository.save(report);

            return new TestResult(test.getId(), "SUCCESS", test.getLogs(), 1000);

        } catch (Exception e) {
            return handleTestFailure(test, report, e);
        }
    }

    public TestResult executeTestWithDelay(TestRequest testRequest) {
        logger.info("Début de l'exécution avec délai du test : {}", testRequest.getTestName());

        TestEntity test = new TestEntity();
        test.setStatus("RUNNING");
        test.setLogs("Test démarré avec délai simulé...");
        testRepository.save(test);

        TestReport report = new TestReport();
        report.setTestName("Delayed Test - " + testRequest.getTestName());
        report.setStartTime(LocalDateTime.now());

        long startTime = System.currentTimeMillis();

        try {
            Thread.sleep(1500);

            long executionTime = System.currentTimeMillis() - startTime;

            test.setStatus("SUCCESS");
            test.setLogs(test.getLogs() + "\nTest terminé avec succès en " + executionTime + " ms.");
            testRepository.save(test);

            report.setStatus("PASSED");
            report.setEndTime(LocalDateTime.now());
            report.setErrorMessage(null);
            testReportRepository.save(report);

            return new TestResult(test.getId(), "SUCCESS", test.getLogs(), executionTime);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return handleTestFailure(test, report, e);
        }
    }

    public List<TestEntity> getAllTests() {
        logger.info("Récupération de tous les tests");
        return testRepository.findAll();
    }

    public String getTestLogs(Long testId) {
        return testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test avec ID " + testId + " introuvable !"))
                .getLogs();
    }

    public boolean cancelTest(Long testId) {
        logger.info("Demande d'annulation du test avec ID : {}", testId);

        Optional<TestEntity> testOpt = testRepository.findById(testId);
        if (!testOpt.isPresent()) {
            throw new ResourceNotFoundException("Test avec ID " + testId + " introuvable !");
        }

        TestEntity test = testOpt.get();
        if ("RUNNING".equals(test.getStatus())) {
            test.setStatus("CANCELLED");
            test.setLogs((test.getLogs() != null ? test.getLogs() : "") + "\nTest annulé.");
            testRepository.save(test);
            logger.info("Test avec ID {} annulé avec succès", testId);
            return true;
        }

        logger.warn("Aucun test RUNNING trouvé avec ID : {}", testId);
        return false;
    }

    public List<TestEntity> getTestsByStatus(String status) {
        if (status == null || status.isEmpty() || status.equalsIgnoreCase("ALL")) {
            logger.info("Filtrage des tests sans statut (tous les tests)");
            return testRepository.findAll();
        }
        logger.info("Filtrage des tests avec statut : {}", status);
        return testRepository.findByStatusIgnoreCase(status);
    }

    private TestResult handleTestFailure(TestEntity test, TestReport report, Exception e) {
        test.setStatus("FAILED");
        test.setLogs((test.getLogs() != null ? test.getLogs() : "") + "\nErreur : " + e.getMessage());
        testRepository.save(test);

        report.setStatus("FAILED");
        report.setErrorMessage(e.getMessage());
        report.setEndTime(LocalDateTime.now());
        testReportRepository.save(report);

        logger.error("Erreur pendant l’exécution du test : {}", e.getMessage());
        return new TestResult(test.getId(), "FAILED", test.getLogs(), 0);
    }
}
