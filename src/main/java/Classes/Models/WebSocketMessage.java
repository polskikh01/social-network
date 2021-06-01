package Classes.Models;

import org.springframework.web.multipart.MultipartFile;

public class WebSocketMessage {

	private Long author;
	
	private Long recipient;
	
	private String message;

	
	private MultipartFile file;
	
	public WebSocketMessage() {
		
	}
	
	public WebSocketMessage(Long author, Long recipient, String message, MultipartFile file) {
		super();
		this.author = author;
		this.recipient = recipient;
		this.message = message;
		this.file = file;
	}

	
	public Long getAuthor() {
		return author;
	}

	public void setAuthor(Long author) {
		this.author = author;
	}

	public Long getRecipient() {
		return recipient;
	}

	public void setRecipient(Long recipient) {
		this.recipient = recipient;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
}
