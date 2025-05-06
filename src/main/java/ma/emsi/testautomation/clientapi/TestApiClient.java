package ma.emsi.testautomation.clientapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class TestApiClient {

    private static final String BASE_URL = "http://localhost:9090/api/tests";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. Exécution d'un test
        TestRequest request = new TestRequest("MonTestAutomatique");
        ResponseEntity<String> runResponse = restTemplate.postForEntity(BASE_URL + "/run", request, String.class);
        System.out.println("Résultat de l'exécution du test : " + runResponse.getBody());

        // 2. Télécharger le PDF
        try {
            String pdfUrl = "http://localhost:9090/api/reports/export/pdf";
            ResponseEntity<byte[]> pdfResponse = restTemplate.getForEntity(pdfUrl, byte[].class);

            if (pdfResponse.getStatusCode().is2xxSuccessful()) {
                byte[] pdfBytes = pdfResponse.getBody();
                try (OutputStream outputStream = new FileOutputStream("rapport-tests.pdf")) {
                    outputStream.write(pdfBytes);
                    System.out.println("✅ PDF sauvegardé sous 'rapport-tests.pdf'");
                }
            } else {
                System.out.println("❌ Erreur lors du téléchargement du PDF : " + pdfResponse.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 3. Lister tous les rapports de tests
        try {
            String listUrl = "http://localhost:9090/api/tests";
            ResponseEntity<TestReport[]> listResponse = restTemplate.getForEntity(listUrl, TestReport[].class);

            if (listResponse.getStatusCode().is2xxSuccessful() && listResponse.getBody() != null) {
                TestReport[] reports = listResponse.getBody();
                System.out.println("📄 Liste des tests :");
                for (TestReport report : reports) {
                    System.out.println(report);
                }
            } else {
                System.out.println("❌ Impossible de récupérer la liste des tests");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


