package Classes.BuisnessLogic;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import Classes.Models.*;

import Classes.Repository.*;

@Service
public class MessageService {

	@Autowired
	private UsersRepository usersRepo;

	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private MessageRepository messageRepo;
	
	@Autowired
	private ContactsRepository contactsRepo;
	
	
	public Messages SaveMessage(WebSocketMessage message) {
		
		
		Messages newMessage = new Messages();
		newMessage.setAuthor(usersRepo.findById(message.getAuthor()).get());
		newMessage.setRecipient(usersRepo.findById(message.getRecipient()).get());
		newMessage.setDateTime(LocalDateTime.now());
		newMessage.setMessage(message.getMessage());
		
		if(message.getFile() != null) {
			
			File uploadDir = new File(uploadPath);
			if(!uploadDir.exists()) {
				uploadDir.mkdir();
			}
			
			String uuidFile = UUID.randomUUID().toString();
			String resultFileName = uuidFile + "." + message.getFile().getOriginalFilename();

			newMessage.setImageUrl(resultFileName);
			try {
				message.getFile().transferTo(new File(uploadPath + "/" + resultFileName));
			}
			catch(Exception e) {
				newMessage.setImageUrl("");
			}
		}
		else
			newMessage.setImageUrl(null);
		
		messageRepo.save(newMessage);
		
		var contact = contactsRepo.getContactsById(message.getAuthor(), message.getRecipient());
		
		if(message.getFile() == null)
			contact.setLastMessage(newMessage.getMessage());
		else 
			contact.setLastMessage("Фото");
		contact.setLastTime(LocalDateTime.now());
		contactsRepo.save(contact);
		
		
		return newMessage;
		
	}


	public void CreateNewContact(String text, Long contactId) {
		Messages message = new Messages();
		message.setAuthor((Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		message.setRecipient(usersRepo.findById(contactId).get());
		message.setDateTime(LocalDateTime.now());
		message.setImageUrl(null);
		
		var contact = new Contacts(Status.Contact, message.getRecipient(), message.getAuthor());
		messageRepo.save(message);
		contactsRepo.save(contact);
		
		
	}

}
