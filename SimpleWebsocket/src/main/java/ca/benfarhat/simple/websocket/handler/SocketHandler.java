package ca.benfarhat.simple.websocket.handler;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.benfarhat.simple.websocket.model.ChatMessage;

@Component
public class SocketHandler extends TextWebSocketHandler {

	/*

	@Autowired
	MessageRepository messageRepository;
	
	*/
	
	private String salon = "DEFAUT";

	public SocketHandler(String salon) {
		super();
		this.salon = salon;
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {

		String payload = message.getPayload();
		JSONObject jsonObject = new JSONObject(payload);
        ObjectMapper Obj = new ObjectMapper();
        
        ChatMessage chatMessage = new ChatMessage(null, null, null);
  
        try { 
            String jsonStr = Obj.writeValueAsString(chatMessage); 
  
            // Displaying JSON String 
            System.out.println(jsonStr); 
        } 
  
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
		session.sendMessage(new TextMessage(Obj.writeValueAsString(jsonObject)));
		//session.sendMessage(new TextMessage(jsonObject.get("message") + "\n"));
	}


	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// The WebSocket has been closed
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// The WebSocket has been opened
		// I might save this session object so that I can send messages to it outside of
		// this method

		// Let's send the first message
		//Gson gson = new Gson();
		//session.sendMessage(new TextMessage(gson.toJson(findall())));
	}

}