package ma.emsi.testautomation.service;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service("ActiveFirst")
public class ActiveFirstService implements WebServiceExecutor {

    @Override
    public String execute(Map<String, Object> params) {
        // Your actual ActiveFirst logic here
        return "✅ ActiveFirst exécuté avec succès";
    }
}