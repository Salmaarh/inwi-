package ma.emsi.testautomation.repository;


import ma.emsi.testautomation.entity.TestExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestExecutionRepository extends JpaRepository<TestExecution, Long> {
}
