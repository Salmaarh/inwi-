package ma.emsi.testautomation.repository;

import ma.emsi.testautomation.entity.ScenarioExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScenarioExecutionLogRepository extends JpaRepository<ScenarioExecutionLog, Long> {
}
