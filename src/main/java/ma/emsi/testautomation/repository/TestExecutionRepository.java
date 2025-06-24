package ma.emsi.testautomation.repository;

import ma.emsi.testautomation.entity.TestExecution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestExecutionRepository extends JpaRepository<TestExecution, Long> {
}
