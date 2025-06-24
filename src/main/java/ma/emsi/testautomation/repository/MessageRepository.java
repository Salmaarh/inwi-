package ma.emsi.testautomation.repository;


import ma.emsi.testautomation.entity.Message;
import ma.emsi.testautomation.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiver(AppUser receiver);
}
