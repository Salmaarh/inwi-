package ma.emsi.testautomation.service;

import ma.emsi.testautomation.entity.scenario;
import ma.emsi.testautomation.entity.ScenarioExecutionLog;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScenarioService {

    private final Map<String, List<scenario>> scenarioMap = new HashMap<>();
    private final List<ScenarioExecutionLog> executionLogs = new ArrayList<>();

    // Sauvegarder un scénario (ajoute à la liste par clé)
    public void saveScenario(scenario s) {
        scenarioMap.computeIfAbsent(s.getServiceKey(), k -> new ArrayList<>()).add(s);
    }

    // 1. Récupérer tous les scénarios par serviceKey
    public List<scenario> getScenariosByService(String serviceKey) {
        return scenarioMap.getOrDefault(serviceKey, Collections.emptyList());
    }

    // 2. Exécuter un scénario par index
    public String executeScenario(String serviceKey, int index) {
        List<scenario> list = getScenariosByService(serviceKey);
        if (index < 0 || index >= list.size()) {
            return "❌ Index invalide pour le service : " + serviceKey;
        }

        scenario s = list.get(index);
        // Ici tu peux exécuter chaque WebService dans le scénario s.getSteps()
        // Exemple : pour (String step : s.getSteps()) { ... }

        // Log
        ScenarioExecutionLog log = new ScenarioExecutionLog();
        log.setScenarioName(s.getName());
        log.setExecutionTime(new Date());
        log.setStatus("SUCCESS"); // ou "FAILED" selon exécution

        executionLogs.add(log);

        return "✅ Scénario exécuté avec succès : " + s.getName();
    }

    // 3. Lister toutes les clés des services
    public Set<String> getAllServiceKeys() {
        return scenarioMap.keySet();
    }

    // 4. Récupérer tous les logs d'exécution
    public List<ScenarioExecutionLog> getAllExecutionLogs() {
        return executionLogs;
    }

    public List<scenario> getscenariosByService(String serviceKey) {
        return null;
    }

    public String executescenario(String serviceKey, int index) {
        return null;
    }

}
