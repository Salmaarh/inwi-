package ma.emsi.testautomation.entity;

import ma.emsi.testautomation.repository.TestWebService;

import java.util.Map;

public class CreateSubscriber implements TestWebService {
    @Override
    public String execute(Map<String, Object> params) {
        return "CreateSubscriber exécuté";
    }
}
