package ma.emsi.testautomation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ScenarioExecutionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scenarioName;
    private String serviceKey;
    private int scenarioIndex;

    private String result;

    private LocalDateTime executionTime;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getScenarioName() { return scenarioName; }
    public void setScenarioName(String scenarioName) { this.scenarioName = scenarioName; }

    public String getServiceKey() { return serviceKey; }
    public void setServiceKey(String serviceKey) { this.serviceKey = serviceKey; }

    public int getScenarioIndex() { return scenarioIndex; }
    public void setScenarioIndex(int scenarioIndex) { this.scenarioIndex = scenarioIndex; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public LocalDateTime getExecutionTime() { return executionTime; }
    public void setExecutionTime(LocalDateTime executionTime) { this.executionTime = executionTime; }
}
