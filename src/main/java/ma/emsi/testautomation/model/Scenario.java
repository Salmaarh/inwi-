package ma.emsi.testautomation.model;
import java.util.List;

public class Scenario {
    private String scenarioName;
    private List<String> services;

    public Scenario() {}

    public Scenario(String scenarioName, List<String> services) {
        this.scenarioName = scenarioName;
        this.services = services;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }
}


