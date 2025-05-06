package ma.emsi.testautomation.clientapi;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class TestExecutorClient {

    public static void main(String[] args) {
        String apiUrl = "http://localhost:9090/api/tests/execute";
        RestTemplate restTemplate = new RestTemplate();

        // Créer l'objet de requête
        String testName = "Test de l'automatisation";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // JSON body
        String jsonBody = "{\"testName\": \"" + testName + "\"}";

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("✅ Test exécuté avec succès !");
            } else {
                System.out.println("❌ Échec d'exécution du test !");
            }

        } catch (Exception e) {
            System.out.println("🚨 Exception : " + e.getMessage());
        }
    }
}

