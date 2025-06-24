package ma.emsi.testautomation.controller;

import ma.emsi.testautomation.entity.scenario;
import ma.emsi.testautomation.entity.ScenarioExecutionLog;
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

    // 1. Lister les scénarios disponibles pour un service
    @GetMapping("/{serviceKey}")
    public List<scenario> getScenarios(@PathVariable String serviceKey) {
        return scenarioService.getscenariosByService(serviceKey);
    }

    // 2. Exécuter un scénario spécifique
    @PostMapping("/execute")
    public ResponseEntity<String> executeScenario(
            @RequestParam String serviceKey,
            @RequestParam int index
    ) {
        String result = scenarioService.executescenario(serviceKey, index);
        return ResponseEntity.ok(result);
    }

    // 3. Lister toutes les clés de services enregistrés
    @GetMapping("/services")
    public Set<String> getAvailableServiceKeys() {
        return scenarioService.getAllServiceKeys();
    }

    // 4. Récupérer tous les logs d'exécution
    @GetMapping("/executions")
    public List<ScenarioExecutionLog> getAllExecutionLogs() {
        return scenarioService.getAllExecutionLogs();
    }
    // 5. Enregistrer un nouveau scénario
    @PostMapping("/save")
    public ResponseEntity<String> saveScenario(@RequestBody scenario scenario) {
        scenarioService.saveScenario(scenario);
        return ResponseEntity.ok("✅ Scénario enregistré avec succès.");
    }

}
