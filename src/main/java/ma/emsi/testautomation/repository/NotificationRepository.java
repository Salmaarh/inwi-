package ma.emsi.testautomation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ma.emsi.testautomation.entity.Notification;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUsername(String username);
}
