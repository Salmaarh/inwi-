package ma.emsi.testautomation.entity;

import ma.emsi.testautomation.repository.TestWebService;

import java.util.Map;

public class DeleteSubscriber implements TestWebService {
    @Override
    public String execute(Map<String, Object> params) {
        // Logique pour supprimer un abonné
        return "DeleteSubscriber exécuté";
    }

}
