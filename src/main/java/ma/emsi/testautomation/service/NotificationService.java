package ma.emsi.testautomation.service;

import ma.emsi.testautomation.entity.Notification;
import ma.emsi.testautomation.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotificationsByUsername(String username) {
        return notificationRepository.findByUsername(username);
    }
}
