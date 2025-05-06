package ma.emsi.testautomation.model;

public class TestResult {
    private Long testId;
    private String status;
    private String logs;
    private long executionTime; // Temps d'exécution en millisecondes

    // Constructeurs
    public TestResult() {
    }

    public TestResult(Long testId, String status, String logs, long executionTime) {
        this.testId = testId;
        this.status = status;
        this.logs = logs;
        this.executionTime = executionTime;
    }

    // Getters et Setters
    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    // Méthode toString (utile pour les logs)
    @Override
    public String toString() {
        return "TestResult{" +
                "testId=" + testId +
                ", status='" + status + '\'' +
                ", logs='" + logs + '\'' +
                ", executionTime=" + executionTime +
                '}';
    }
}
