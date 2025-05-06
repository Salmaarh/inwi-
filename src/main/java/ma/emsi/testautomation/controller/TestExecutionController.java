package ma.emsi.testautomation.controller;

import ma.emsi.testautomation.entity.TestExecution;
import ma.emsi.testautomation.service.GroovyExecutionService;
import ma.emsi.testautomation.service.TestExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tests/execution")  // Ajout du préfixe pour éviter le conflit
public class TestExecutionController {
    private final TestExecutionService service;

    @Autowired
    private GroovyExecutionService groovyExecutionService;

    public TestExecutionController(TestExecutionService service) {
        this.service = service;
    }

    @PostMapping("/run")
    public TestExecution executeTest(@RequestParam String testName) {
        return service.runTest(testName);
    }

    @GetMapping("/all")
    public List<TestExecution> getAllTests() {
        return service.getAllTests();
    }

    @GetMapping("/run-groovy")
    public String executeGroovy() {
        String script = "def runTest() { return 'Test exécuté avec succès' }; runTest()";
        return groovyExecutionService.executeGroovyScript(script);
    }
}
