package ca.benfarhat.simple.websocket.handler;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketChatHandler extends TextWebSocketHandler {

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {

		String payload = message.getPayload();
		session.sendMessage(new TextMessage(payload));
	}


	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// The WebSocket has been closed
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// The WebSocket has been opened


	}

}