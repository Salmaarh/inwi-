package ma.emsi.testautomation.entity;

import jakarta.persistence.*;
import ma.emsi.testautomation.model.TestEntity;

@Entity
@Table(name = "execution_results")
public class ExecutionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private TestEntity test;
    private String status;
    private Long executionTime;

    // Getters et Setters
}

