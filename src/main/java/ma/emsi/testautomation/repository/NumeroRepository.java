package ma.emsi.testautomation.repository;

import ma.emsi.testautomation.entity.Numero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NumeroRepository extends JpaRepository<Numero, Long> {
}
