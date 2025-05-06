package ma.emsi.testautomation.repository;

import ma.emsi.testautomation.model.TestReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestReportRepository extends JpaRepository<TestReport, Long> {
    List<TestReport> findByStatus(String status);
}
