package ma.emsi.testautomation.clientapi;


public class TestRequest {
    private String testName;

    public TestRequest() {}

    public TestRequest(String testName) {
        this.testName = testName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}
