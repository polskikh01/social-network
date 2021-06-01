package Classes.BuisnessLogic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import Classes.Models.*;

import Classes.Repository.*;

@Service
public class UserService implements UserDetailsService {

	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private UsersRepository repository;
	
	@Autowired
	private NewsRepository newsRepo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private MessageRepository messageRepo;
	
	@Autowired
	private ContactsRepository contactsRepo;
	
	@Autowired
	private EmailSenderService mailSender;
	

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Users user = repository.findByUsername(username);
		System.out.println(user.getUsername());

		return user;

		
	}
	
	public Users getUser()
	{
		return (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	

	public Boolean CreateAccount(Users user, String password, Model model) {
		
		var u = repository.findByUsername(user.getUsername());
		if( u != null) {
			
			model.addAttribute("answer", "Аккаунт с указанной почтой уже существует");
			return false;
		}
		
		if(!user.getPassword().equals(password)) {
			
			model.addAttribute("answer", "Пароли не совпадают");
			return false;
		}
		user.setPassword(encoder.encode(user.getPassword()));

		user.setState(AccountState.Created);
		
		
		String confirmValue = UUID.randomUUID().toString();
		user.setConfirmValue(confirmValue);
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUsername());
        
        mailMessage.setSubject("Завершение регистрации");
        mailMessage.setFrom("skorneev00@gmail.com");
        
        mailMessage.setText("Для подтверждения регистрации перейдите по ссылке : "
        +"http://localhost:8000/confirm-account?value=" + confirmValue + "&user=" + user.getUsername());
       
        
		try {
			mailSender.sendEmail(mailMessage);
		}
		catch(MailException e) {
			System.out.println("Неверно указан электронный адрес");
			model.addAttribute("answer", "Неверно указан электронный адрес");
			return false;
		}
		
		user.setInfo(new UserInfo());
		user.getInfo().setAvatar(null);
		repository.save(user);
		
		
		return true;
		
	}
	
	public Users getUser(Long id) {
		return repository.findById(id).get();
	}
	
	public void SaveUser(Users user) {
		repository.save(user);
	}

	public List<News> getNews(Users author){
		
		var list = newsRepo.findAllByAuthor(author);
		
		for(var s: list) {
			System.out.print(s.getText());
		}
		return list;
	}

	public List<Messages> getMessages(Long id, Long contactId) {
		

		return messageRepo.findDialog(id, contactId);
	}
	
	public List<Contacts> getContacts(Long id){
		
		var contacts = contactsRepo.findAllContacts(id);
		long user_id = getUser().getId();
		for(var c: contacts)
		{
			c.reverceContact(user_id);
		}
		return contacts;
	}
	
	public Contacts getContact(Long id)
	{
		
		
		var contact = contactsRepo.findById(id).get();
		contact.reverceContact(getUser().getId());
		return contact;

	}
	
	
	
	public List<Contacts> getFriends(){
		
		var result = contactsRepo.getFriends(getUser().getId());
		
		for(var r : result) {
			r.reverceContact(getUser().getId());
		}
		return result;
		
	}


	public void SetUserInfo(UserInfo info, MultipartFile file) {
		var user = getUser();
		if(file != null && file.getOriginalFilename().length() != 0) {
			File uploadDir = new File(uploadPath);
			if(!uploadDir.exists()) {
				uploadDir.mkdir();
			}
			
			String uuidFile = UUID.randomUUID().toString();
			String resultFileName = uuidFile + "." + file.getOriginalFilename();

			info.setAvatar(resultFileName);
			try {
				file.transferTo(new File(uploadPath + "/" + resultFileName));
			}
			catch(Exception e) {
				System.out.println(e.toString());
				info.setAvatar(null);
			}
		}
		user.getInfo().setInfo(info);
		repository.save(user);
	}
	
	
	public void SetProfileData(Model model) {
		
		
		var user = getUser();


		repository.save(user);

		model.addAttribute("myPage", true);
		model.addAttribute("user", user);

	}

	public void SetOtherProfile(Model model, Long userId) {
		
		model.addAttribute("myPage", false);
		
		Contacts contact;
		try {
			contact = contactsRepo.getContactsById(getUser().getId(), userId);
		}
		catch(Exception e) {
			contact = null;
			
		}
		
		model.addAttribute("user", repository.findById(userId).get());
		model.addAttribute("contact", contact);
	}

	
	public void ChangePassword(String newPassword, String oldPassword, Model model) {
		
		var user = getUser();
		
		if(!encoder.matches(oldPassword, user.getPassword())) {
			System.out.print("Not Equals");
			model.addAttribute("answer", "Неверный старый пароль");
			return;
			
		}
		
		user.setPassword(encoder.encode(newPassword));
		repository.save(user);
		
	}

	public Contacts createContact(Long contactId) {
		var userId = getUser().getId();
		try {
			var contact = contactsRepo.getContactsById(userId, contactId);
			contact.reverceContact(userId);
			return contact;
		}
		catch(Exception e) {
			var contact = new Contacts(Status.Contact, getUser(contactId), getUser());
			
			return contactsRepo.save(contact);
		}
	}
	
	public List<Users> searchFriends(String mainString, String work, String study, String hobby){

		
		var contacts = contactsRepo.getFriends(getUser().getId());
		
		contacts.addAll(contactsRepo.getRequests(getUser().getId()));
		for(var c: contacts) {
			c.reverceContact(getUser().getId());
		}
		var contactsId = new ArrayList<Long>();
		
		for(var c: contacts) {
			contactsId.add(c.getContact().getId());
		}
		contactsId.add(getUser().getId());
		
		String[] names = mainString.split(" ");
		
		List<Users> listWithNames;
		if(names.length == 1) {
			listWithNames = repository.findAllByName(names[0]);
		}
		else 
			listWithNames = repository.findByNames(names[0], names[1]);
		
		Boolean skipExtraParameters = false;
		
		if(work.length() == 0 && study.length() == 0 && hobby.length() == 0) {
			skipExtraParameters = true;
		}
		
		
		List<Users> finalList = new ArrayList<Users>();
		for(var user: listWithNames) {
			if(contactsId.contains(user.getId()))
				continue;
			
			if(skipExtraParameters)
			{
				finalList.add(user);
				continue;
			}
			
			if(work.length() != 0 && user.getInfo().getWorkPlace() != null) {
				if(!user.getInfo().getWorkPlace().contains(work))
					continue;
				
			}
			
			if(study.length() != 0 && user.getInfo().getStudyPlace() != null) {
				if(!user.getInfo().getStudyPlace().contains(study))
					continue;
				
			}
			
			if(hobby.length() != 0 && user.getInfo().getHobby() != null) {
				if(!user.getInfo().getHobby().contains(hobby))
					continue;
				
			}
			
			finalList.add(user);
		}
		
		return finalList;
	}

	
	public void updateContact(WebSocketAction info ) {
		
		switch(info.getAction()) {
		
			case "request":{ 
				Contacts contact;
				try {
					contact = contactsRepo.getContactsById(info.getUser(), info.getContact());
					contact.reverceContact(info.getUser());
				}
				catch(Exception e) {
					System.out.print(e.toString());
					contact = new Contacts(Status.Contact, getUser(info.getContact()), getUser(info.getUser()));
				}
				contact.setStatus(Status.Request);
				contactsRepo.save(contact);
				
				break;
			}
			case "accept" : {
				var contact = contactsRepo.getContactsById(info.getUser(), info.getContact());
				contact.setStatus(Status.Friends);
				contactsRepo.save(contact);
				break;
			}
			case "decline": {
				var contact = contactsRepo.getContactsById(info.getUser(), info.getContact());
				contact.setStatus(Status.Contact);
				contactsRepo.save(contact);
				break;
			}
			default : break;
		
		}
		
	}
	
	public List<Contacts> getRequests() {
		
		
		return contactsRepo.getAllRequests(getUser().getId());
	}
	
	public void sendRequest(Long id) {
		var contact = createContact(id);
		
		contact.setStatus(Status.Request);
		contactsRepo.save(contact);
	}
	
	public void setBlocking(String action, Long userId) {
		if(!getUser().getIsAdmin()) {
			return;
		}
		
		var user = getUser(userId);
		if(action.equals("block")) {
			user.setState(AccountState.Blocked);
		}
		else if(action.equals("decline")) {
			user.setState(AccountState.Active);
			
		}
		
		repository.save(user);
	}
}
