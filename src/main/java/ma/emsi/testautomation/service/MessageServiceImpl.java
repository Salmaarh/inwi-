package ma.emsi.testautomation.service;

import ma.emsi.testautomation.entity.Message;
import ma.emsi.testautomation.model.AppUser;
import ma.emsi.testautomation.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void sendMessage(Optional<AppUser> sender, Optional<AppUser> receiver, String content) {
        Message message = new Message();
        sender.ifPresent(message::setSender);
        receiver.ifPresent(message::setReceiver);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        messageRepository.save(message);
    }


    @Override
    public List<Message> getMessages(AppUser user) {
        return messageRepository.findByReceiver(user);
    }

    @Override
    public List<String> getDistinctUsernames(AppUser user) {
        List<Message> allMessages = messageRepository.findAll();

        return allMessages.stream()
                .filter(message -> message.getSender().equals(user) || message.getReceiver().equals(user))
                .flatMap(message -> Stream.of(message.getSender(), message.getReceiver()))
                .filter(u -> u != null && !u.equals(user))
                .map(AppUser::getUsername)
                .distinct()
                .collect(Collectors.toList());
    }
    @Override
    public List<Message> getMessagesBetweenUsers(AppUser user1, Optional<AppUser> user2Optional) {
        List<AppUser> users = new ArrayList<>();
        user2Optional.ifPresent(users::add);
        users.add(user1);

        return messageRepository.findAll().stream()
                .filter(message -> {
                    AppUser sender = message.getSender();
                    AppUser receiver = message.getReceiver();
                    return (sender.equals(user1) && users.contains(receiver)) ||
                            (receiver.equals(user1) && users.contains(sender));
                })
                .collect(Collectors.toList());
    }


}
