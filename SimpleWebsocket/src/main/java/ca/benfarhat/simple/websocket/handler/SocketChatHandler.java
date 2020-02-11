package ca.benfarhat.simple.websocket.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketChatHandler extends TextWebSocketHandler {
	private static final Logger log = LoggerFactory.getLogger(SocketChatHandler.class);
	private Map<WebSocketSession, String> sessions = new HashMap<>();
	private List<String> messagesHistory = new ArrayList<>();
	private List<String> usernames = new ArrayList<String>();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {

		String payload = message.getPayload();

		// On en profite pour recupérer le nom de l'utilisateur
		JSONObject jsonObject = new JSONObject(payload);
		sessions.replace(session, null, (String) jsonObject.get("user"));

		switch (String.valueOf(jsonObject.get("action"))) {
		case "sendMessage":
			sessions.forEach((webSocketSession, username) -> {
				try {
					System.out.println(payload);
					webSocketSession.sendMessage(new TextMessage(payload));
					messagesHistory.add(payload);
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			});
			break;
		case "changeName":
			String oldName = sessions.get(session);
			String newName = String.valueOf(jsonObject.get("user"));
			sessions.replace(session, String.valueOf(jsonObject.get("user")));
			sendUserList();
			if(Objects.isNull(oldName)) {
				if(Strings.isBlank(newName)) {
					broadcastInformation(sessions, "Un utilisateur a rejoint la session");
				} else {
					broadcastInformation(sessions, newName + " a rejoint la session");
				}
				
			} else {
				broadcastInformation(sessions, oldName + " a changé son nickname en " + newName);
			}

			break;
		default:
		}

	}

	private void sendUserList() {

		usernames = sessions.entrySet().stream().filter(session -> !Strings.isEmpty(session.getValue()))
				.map(Map.Entry::getValue).collect(Collectors.toList());

		sessions.forEach((webSocketSession, username) -> {

			JSONObject response = new JSONObject();
			response.append("action", "updateUsername");
			response.append("usernames", usernames);
			try {
				webSocketSession.sendMessage(new TextMessage(response.toString()));
			} catch (IOException e) {
				log.info(e.getMessage());
			}

		});
	}

	@Override 
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		String username = sessions.get(session);
		
		sessions.remove(session);
		log.info("Un utilisateur a quitter la conversation.");
		sendUserList();
		broadcastInformation(sessions, username + " a quitté la session");
	} 
 
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.put(session, null);
		log.info("Un utilisateur a rejoint la conversation.");
		sendUserList();
		messagesHistory.forEach(payload -> {
			try {
				session.sendMessage(new TextMessage(payload));
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		});

	}

	public void broadcastInformation(Map<WebSocketSession, String> sessions, String message) {
		sessions.forEach((webSocketSession, username) -> {sendInformation(webSocketSession, message);});
	}

	public void sendInformation(WebSocketSession session, String message) {
		JSONObject response = new JSONObject();
		response.append("action", "information");
		response.append("message", message);
		try {
			session.sendMessage(new TextMessage(response.toString()));
		} catch (IOException e) {
			log.info(e.getMessage());
		}
	}

}