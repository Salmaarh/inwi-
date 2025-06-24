package ma.emsi.testautomation.entity;

import java.util.Date;
import java.util.List;

public class scenario {
    private String name;
    private List<String> services;
    private Date executionTime;

    public scenario() {}

    public scenario(String name, List<String> services) {
        this.name = name;
        this.services = services;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public String getServiceKey() {
        return null;
    }

    public Throwable getSteps() {
        return null;
    }
}
