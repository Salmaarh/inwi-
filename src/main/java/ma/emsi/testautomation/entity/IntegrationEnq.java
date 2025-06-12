package ma.emsi.testautomation.entity;

import ma.emsi.testautomation.repository.TestWebService;

import java.util.Map;

public class IntegrationEnq implements TestWebService {
    @Override
    public String execute(Map<String, Object> params) {
        return "IntegrationEnq exécuté";
    }
}
