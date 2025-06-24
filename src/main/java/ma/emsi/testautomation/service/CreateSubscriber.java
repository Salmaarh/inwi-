package ma.emsi.testautomation.service;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service("CreateSubscriber")
public class CreateSubscriber implements WebServiceExecutor {

    @Override
    public String execute(Map<String, Object> params) {

        String number = (String) params.getOrDefault("number", "aucun numéro");
        return "✅ CreateSubscriber exécuté pour le numéro : " + number;
    }
}
