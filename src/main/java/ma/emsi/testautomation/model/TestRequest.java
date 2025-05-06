package ma.emsi.testautomation.model;

import java.util.Map;

public class TestRequest {
    private String testName;
    private Map<String, Object> parameters;
    private String script; //  Ajout du script

    // Constructeurs
    public TestRequest() {
    }

    public TestRequest(String testName, Map<String, Object> parameters, String script) {
        this.testName = testName;
        this.parameters = parameters;
        this.script = script;
    }

    // Getters et Setters
    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    public String toString() {
        return "TestRequest{" +
                "testName='" + testName + '\'' +
                ", parameters=" + parameters +
                ", script='" + script + '\'' +
                '}';
    }
}
