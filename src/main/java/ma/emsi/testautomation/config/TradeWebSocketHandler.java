package ma.emsi.testautomation.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ma.emsi.testautomation.model.Message;
import ma.emsi.testautomation.repository.MessageRepository;
import  ma.emsi.testautomation.repository.UserRepository;
import ma.emsi.testautomation.model.AppUser;
import ma.emsi.testautomation.service.MessageService;
import ma.emsi.testautomation.service.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TradeWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageservice;
    private final Map<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();
    @Override

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        String username = null;
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2 && "username".equals(pair[0])) {
                    username = URLDecoder.decode(pair[1], StandardCharsets.UTF_8.toString());
                    break;
                }
            }
        }

        if (username != null) {
            session.getAttributes().put("username", username);
            activeSessions.put(username, session);
            System.out.println("Nouvelle session WebSocket établie pour l'utilisateur: " + username);
            System.out.println("Nombre total de sessions actives: " + activeSessions.size());
        } else {
            System.err.println("Impossible d'établir la session WebSocket : nom d'utilisateur manquant");
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(payload);

        // Extract 'to' and 'message' fields from the JSON payload
        String to = jsonNode.get("to").asText();
        String content = jsonNode.get("message").asText();

        // Extract sender's username from the WebSocket URL
        String senderUsername = session.getUri().getQuery().split("=")[1];

        // Retrieve sender and receiver from the database
        Optional<AppUser> sender = userRepository.findByUsername(senderUsername);
        Optional<AppUser> receiver = userRepository.findByUsername(to);

        if (sender.isPresent() && receiver.isPresent()) {
            ObjectNode messageNode = objectMapper.createObjectNode();
            messageNode.put("from", senderUsername);
            messageNode.put("message", content);
            String messageToSend = objectMapper.writeValueAsString(messageNode);

            // Send the message to the recipient user if session is open
            WebSocketSession recipientSession = activeSessions.get(to);
            if (recipientSession != null && recipientSession.isOpen()) {
                recipientSession.sendMessage(new TextMessage(messageToSend));
            } else {
                System.err.println("Recipient session is closed: " + to);
                // Handle closed session according to your requirements
            }

            // Save the message to the database
            messageservice.sendMessage(Optional.of(sender.get()), Optional.of(receiver.get()), content);
        } else {
            System.err.println("Sender or receiver not found in database");
            // Handle missing sender or receiver according to your requirements
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = (String) session.getAttributes().get("username");
        if (username != null) {
            activeSessions.remove(username);
        }
    }

}
