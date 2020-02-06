package ca.benfarhat.simple.websocket.model;

public class ChatMessage {
	
	private long id;

	private String username;
	
	private String message;
	
	private String salon;

	public ChatMessage(String username, String message, String salon) {
		super();
		this.id = System.currentTimeMillis();
		this.username = username;
		this.message = message;
		this.salon = salon;
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getMessage() {
		return message;
	}

	public String getSalon() {
		return salon;
	}
	
	

}
