package ma.emsi.testautomation.service;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service("AdjustAccount")
public class AdjustAccountService implements WebServiceExecutor {

    @Override
    public String execute(Map<String, Object> params) {
        // Your actual AdjustAccount logic here
        return "✅ AdjustAccount exécuté avec succès";
    }
}