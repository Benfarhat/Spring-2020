package ca.benfarhat.simple.websocket.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	Map<WebSocketSession, String> sessions = new HashMap<>();
	List<String> messagesHistory = new ArrayList<>();
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		
		String payload = message.getPayload();
		
		// On en profite pour recupérer le nom de l'utilisateur
		JSONObject jsonObject = new JSONObject(payload);
		sessions.replace(session, null, (String) jsonObject.get("user"));

		switch(String.valueOf(jsonObject.get("action"))) {
		case "sendMessage":
			sessions.forEach((webSocketSession,username)->{
				try {
					webSocketSession.sendMessage(new TextMessage(payload));
					messagesHistory.add(payload);
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			});
		break;
		case "changeName":
			sessions.replace(session, String.valueOf(jsonObject.get("user")));
			break;
		default:
		}


	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		log.info("Un utilisateur a quitter la conversation.");
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.put(session, null);
		log.info("Un utilisateur a rejoint la conversation.");
		
		messagesHistory.forEach(payload ->{
			try {
				session.sendMessage(new TextMessage(payload));
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		});
		
	}

}