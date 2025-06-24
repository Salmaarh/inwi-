package ma.emsi.testautomation.service;

import ma.emsi.testautomation.entity.TestExecution;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface WebServiceExecutor {
    String execute(Map<String, Object> params);

    @Service
    public class WebServiceexecutor implements ma.emsi.testautomation.repository.WebServiceExecutor {

        public String executeCreateSubscriber(Map<String, Object> params) {
            // Appel du Web Service ou JAR ici
            return "CreateSubscriber exécuté";
        }

        public String executeDeleteSubscriber(Map<String, Object> params) {
            return "DeleteSubscriber exécuté";
        }

        public String executeAdjustAccount(Map<String, Object> params) {
            return "AdjustAccount exécuté";
        }

        public String executeActiveFirst(Map<String, Object> params) {
            return "ActiveFirst exécuté";
        }

        public String executeIntegrationEnq(Map<String, Object> params) {
            return "IntegrationEnq exécuté";
        }


        @Override
        public String execute(Map<String, Object> params) {
            return "";
            //db hhna 9adi lcode d execute bdebt chnu adir wbn li bli kata5d des parametres
        }

//    @Override
//    public List<TestExecution> findAll() {
//        return List.of();
//    }
//
//    @Override
//    public TestExecution save(TestExecution testExecution) {
//        return null;
//    }
    }
}