package ma.emsi.testautomation.repository;

import ma.emsi.testautomation.model.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestRepository extends JpaRepository<TestEntity, Long> {

    // Méthode automatique par nom
    List<TestEntity> findByStatusIgnoreCase(String status);

    // Méthode personnalisée avec @Query
    @Query("SELECT t FROM TestEntity t WHERE t.status = :status")
    List<TestEntity> findByStatus(@Param("status") String status);
}
