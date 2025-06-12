package ma.emsi.testautomation.controller;

import ma.emsi.testautomation.entity.ScenarioExecutionLog;
import ma.emsi.testautomation.entity.scenario;
import ma.emsi.testautomation.service.ScenarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/scenarios")
@CrossOrigin(origins = "*")
public class ScenarioController {

    private final ScenarioService scenarioService;

    public ScenarioController(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    // 1. Récupérer les scénarios pour un service donné
    @GetMapping("/{serviceKey}")
    public List<scenario> getScenarios(@PathVariable String serviceKey) {
        return scenarioService.getScenariosByService(serviceKey);
    }

    //  2. Exécuter un scénario donné
    @PostMapping("/execute")
    public ResponseEntity<String> executeScenario(
            @RequestParam String serviceKey,
            @RequestParam int index
    ) {
        String result = scenarioService.executeScenario(serviceKey, index);
        return ResponseEntity.ok(result);
    }

    //  3. Nouveau : lister toutes les clés de services disponibles
    @GetMapping("/services")
    public Set<String> getAvailableServiceKeys() {
        return scenarioService.getAllServiceKeys();
    }

    //  4. Nouveau : récupérer tous les résultats d’exécution des scénarios
    @GetMapping("/executions")
    public List<ScenarioExecutionLog> getAllExecutionLogs() {
        return scenarioService.getAllExecutionLogs();
    }
}
