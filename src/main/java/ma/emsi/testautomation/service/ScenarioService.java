package ma.emsi.testautomation.service;

import jakarta.annotation.PostConstruct;
import ma.emsi.testautomation.entity.ScenarioExecutionLog;
import ma.emsi.testautomation.entity.scenario;
import ma.emsi.testautomation.entity.step;
import ma.emsi.testautomation.repository.ScenarioExecutionLogRepository;
import ma.emsi.testautomation.registry.WebServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ScenarioService {

    @Autowired
    private ScenarioExecutionLogRepository logRepository;

    @Autowired
    private WebServiceRegistry webServiceRegistry;

    private final Map<String, List<scenario>> scenarioMap = new HashMap<>();

    @PostConstruct
    public void initScenarios() {
        scenarioMap.put("authentication-service", List.of(
                new scenario("Création Abonné Standard", List.of(
                        new step("Endpoint", "CreateSubscriber"),
                        new step("Méthode", "POST"),
                        new step("Validation", "Email & Phone"),
                        new step("Timeout", "5000ms"),
                        new step("Retry", "3x")
                )),
                new scenario("Vérification Abonné", List.of(
                        new step("Endpoint", "VerifySubscriber"),
                        new step("Méthode", "GET"),
                        new step("Headers", "Authorization Bearer"),
                        new step("Timeout", "3000ms")
                ))
        ));
    }

    public List<scenario> getScenariosByService(String serviceKey) {
        return scenarioMap.getOrDefault(serviceKey, List.of());
    }

    public String executeScenario(String serviceKey, int index) {
        List<scenario> scenarios = scenarioMap.get(serviceKey);
        if (scenarios == null || index < 0 || index >= scenarios.size()) {
            return "❌ Scénario non trouvé.";
        }

        scenario selected = scenarios.get(index);
        StringBuilder result = new StringBuilder();
        result.append("📋 Scénario : ").append(selected.getName()).append("\n");

        for (step step : selected.getsteps()) {
            result.append("➡️ Étape : ").append(step.getKey()).append(" => ").append(step.getValue()).append("\n");

            if ("Endpoint".equalsIgnoreCase(step.getKey())) {
                String serviceName = step.getValue();
                WebServiceExecutor webService = webServiceRegistry.getWebService(serviceName);

                if (webService != null) {
                    // 💡 Paramètres simulés
                    Map<String, Object> params = new HashMap<>();
                    params.put("msisdn", "212600000000");
                    params.put("operationId", UUID.randomUUID().toString());
                    params.put("timestamp", LocalDateTime.now());

                    String executionResult = webService.execute(params);

                    result.append("✅ Exécution [").append(serviceName).append("] : ")
                            .append(executionResult).append("\n");
                } else {
                    result.append("❌ WebService non trouvé : ").append(serviceName).append("\n");
                }
            }
        }

        // 💾 Journaliser l'exécution
        ScenarioExecutionLog log = new ScenarioExecutionLog();
        log.setScenarioName(selected.getName());
        log.setServiceKey(serviceKey);
        log.setScenarioIndex(index);
        log.setResult(result.toString());
        log.setExecutionTime(LocalDateTime.now());

        logRepository.save(log);

        return result.toString();
    }

    public Set<String> getAllServiceKeys() {
        return scenarioMap.keySet();  // retourne toutes les clés (noms de services)
    }

    public List<ScenarioExecutionLog> getAllExecutionLogs() {
        return logRepository.findAll();  // retourne tous les logs depuis la BDD
    }

}
