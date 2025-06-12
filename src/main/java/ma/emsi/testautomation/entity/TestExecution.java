package ma.emsi.testautomation.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class TestExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testName;

    private String status; // "SUCCESS", "FAILURE", etc.

    @Lob
    private String params; // Pour enregistrer les paramètres d'entrée

    @Lob
    private String result; // Résultat ou message de retour du test

    private LocalDateTime executionTime;
}
