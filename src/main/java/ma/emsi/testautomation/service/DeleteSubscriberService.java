package ma.emsi.testautomation.service;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service("DeleteSubscriber")
public class DeleteSubscriberService implements WebServiceExecutor {

    @Override
    public String execute(Map<String, Object> params) {
        // Your actual DeleteSubscriber logic here
        return "✅ DeleteSubscriber exécuté avec succès";
    }
}