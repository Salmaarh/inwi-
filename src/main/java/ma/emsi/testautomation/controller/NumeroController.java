package ma.emsi.testautomation.controller;

import ma.emsi.testautomation.service.NumeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/numeros")
@CrossOrigin(origins = "*") // pour permettre les appels depuis ton front (React par ex.)
public class NumeroController {

    @Autowired
    private NumeroService numeroService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Le fichier est vide.");
        }

        String response = numeroService.saveNumerosFromFile(file);
        return ResponseEntity.ok(response);
    }
}
