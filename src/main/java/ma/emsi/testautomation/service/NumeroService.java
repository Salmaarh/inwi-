package ma.emsi.testautomation.service;

import ma.emsi.testautomation.entity.Numero;
import ma.emsi.testautomation.repository.NumeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class NumeroService {

    @Autowired
    private NumeroRepository numeroRepository;

    public String saveNumerosFromFile(MultipartFile file) {
        List<Numero> numeros = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String numeroValue = line.trim();

                if (!numeroValue.isEmpty()) {
                    Numero numero = new Numero();
                    numero.setValeur(numeroValue);
                    numeros.add(numero);
                }
            }

            numeroRepository.saveAll(numeros);
            return "Upload réussi : " + numeros.size() + " numéros enregistrés.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'upload du fichier.";
        }
    }
}
