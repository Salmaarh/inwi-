package ma.emsi.testautomation.model;


import ma.emsi.testautomation.model.Message;
import ma.emsi.testautomation.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface Message {
    void sendMessage(Optional<AppUser> sender, Optional<AppUser> receiver, String content);
    List<Message> getMessages(AppUser user);
    List<String> getDistinctUsernames(AppUser user);

    List<Message> getMessagesBetweenUsers(AppUser user1, Optional<AppUser> user2);
}

