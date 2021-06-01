package Classes.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import Classes.BuisnessLogic.*;
import Classes.Models.*;




@Controller
public class WebSocketController {

	
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private MessageService service;

	
	@Autowired
	private NewsService newsServ;
	
	@Autowired
	private UserService userService;
	
	@MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable String to, WebSocketMessage message) {
		
		
        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, service.SaveMessage(message));
    }
	
	@MessageMapping("/news-comment")
	public void setComment(WebSocketComment comm) {
		
		
		newsServ.SetComment(comm);
	}
	
	@MessageMapping("/contact")
	public void updateContact(WebSocketAction info) {
		userService.updateContact(info);
	}
	
	@MessageMapping("/news-likes")
	public void setLike(Likes value)
	{
		//System.out.print("connect");
		newsServ.setLike(value);
	}
}
