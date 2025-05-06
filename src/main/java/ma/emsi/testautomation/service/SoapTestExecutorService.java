package ma.emsi.testautomation.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@Service
public class SoapTestExecutorService {

    public void executeSoapUiProject(String filePath) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            NodeList testSteps = document.getElementsByTagName("con:request");

            for (int i = 0; i < testSteps.getLength(); i++) {
                Element step = (Element) testSteps.item(i);
                String requestXml = step.getTextContent();

                // URL du web service (tu peux aussi l'extraire du fichier si elle change)
                String endpoint = "http:/CHF-soapui-project.xml/soap-endpoint"; // À adapter

                String result = sendSoapRequest(requestXml, endpoint);

                // Sauvegarde ou log du résultat
                System.out.println("Réponse SOAP : " + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String sendSoapRequest(String xml, String endpoint) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(xml.getBytes());
        }

        InputStream responseStream = conn.getInputStream();
        return new BufferedReader(new InputStreamReader(responseStream))
                .lines().collect(Collectors.joining("\n"));
    }
}
