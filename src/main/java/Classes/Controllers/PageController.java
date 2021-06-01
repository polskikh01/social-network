package Classes.Controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Classes.BuisnessLogic.*;
import Classes.Models.*;


@Controller
public class PageController {

	
	
	
	@Autowired
	private UserService service;
	
	@Autowired
	private MessageService messageServ;
	
	@Autowired
	private NewsService newsServ;
	
	
	@GetMapping("/Dialogs")
	public String greeting( Model model) {

		model.addAttribute("IsDialogList", true);
		var user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("Contacts", service.getContacts(user.getId()));
		model.addAttribute("User", user.getId());
		return "Dialog";
	}
	
	@GetMapping("/Dialogs/person")
	public String getDialogWithPerson(@RequestParam String contactId, Model model) {

		var user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		var contact = service.getContact(Long.parseLong(contactId));
		
		long cont_User = contact.getUser().getId();
		long cont_Recip = contact.getContact().getId();
		var dialogList = service.getMessages(cont_User, cont_Recip);
		
		model.addAttribute("Dialog", dialogList);
		model.addAttribute("Contact", contact.getContact());
		model.addAttribute("ChatName", contactId);
		model.addAttribute("User", user.getId());
		model.addAttribute("IsDialogList", false);
		return "Dialog";
	}
	

	@PostMapping("/newMessage")
	public String writeNewMessage(@RequestParam String contactId, @RequestParam String text, Model model) {
		
		
		messageServ.CreateNewContact(text, Long.parseLong(contactId));
		return "redirect:/Dialogs/person?contactId=" + contactId;
		
	}
	

	@GetMapping("/News")
	public String newsPage(@RequestParam String type,  Model model) {
		
	 
		newsServ.getAllNews(model, type);
		
		return "News";
	}


	@PostMapping("/Dialogs/file")
	private String loadImage(@RequestParam("file") MultipartFile file,
							 @RequestParam String contactId,
							 @RequestParam String recipient,
							 
							 Model model)
			
	{
		var user  = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		WebSocketMessage message = new WebSocketMessage(user.getId(), Long.parseLong(recipient), "", file);
		
		messageServ.SaveMessage(message);
		
		
		return "redirect:/Dialogs/person?contactId=" + contactId;
	}
	
	
	@GetMapping("/Friends")
	public String friendPage(@RequestParam String type, Model model) {
		var user  = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		model.addAttribute("type", type);
		model.addAttribute("UserId", user.getId());
		if(type.equals("myFriends")) {
			model.addAttribute("Friends", service.getFriends());
			
		}
		else if(type.equals("requests")){
			model.addAttribute("Requests", service.getRequests());
		}
		else if(type.equals("search"))
		{
			if(!model.containsAttribute("users")) {
				model.addAttribute("users", new ArrayList<Users>());
			}
		}
			
		
		return "Friends";
	}
	
	@GetMapping("/")
	public String profilPage(Model model) {
		
		
		service.SetProfileData(model);
		if(!model.containsAttribute("answer"))
			model.addAttribute("answer", null);
		return "ProfilPage";
	}
	 
	 @PostMapping("/updateUser")
	 public String update(
			 @RequestParam("avatar") MultipartFile file,
			 @RequestParam String about,
			 @RequestParam String date, 
			 @RequestParam String workPlace,
			 @RequestParam String studyPlace,
			 @RequestParam String hobby,
			 Model model
			 )
	 {
		
		UserInfo info = new UserInfo(about, date, studyPlace, workPlace, hobby);

		service.SetUserInfo(info, file);
		
		//service.SetProfileData(model);	
		return "redirect:/";
		 
	 }
	 
	 @GetMapping("/user")
	 public String getOtherPage(@RequestParam String userId, Model model) {
		 var user  = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 if(userId.equals(user.getId().toString()))
			 return "redirect:/";
		 service.SetOtherProfile(model, Long.parseLong(userId));
		 
		 model.addAttribute("admin", user.getIsAdmin());
		 return "ProfilPage";
	 }
	 
	 @PostMapping("/changePassword")
	 public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirm, Model model ) {
		 
		 
		 if(!newPassword.equals(confirm))
		 {
			 model.addAttribute("answer", "Пароли не совпадают");
			 return "ProfilPage";
		 }
		 service.ChangePassword(newPassword, oldPassword, model);
		 service.SetProfileData(model);	
		 return "redirect:/";
	 }


	 @PostMapping("/News/addNew")
	 public String addNew(@RequestParam String text) {
		 
		 newsServ.CreateNew(text);
		 return "redirect:/News?type=new";
	 }
	

	 @GetMapping("/Dialogs/createContact")
	 public String createContact(@RequestParam String userId) {
		 
		 
		 return "redirect:/Dialogs/person?contactId=" + service.createContact(Long.parseLong(userId)).getId();
	 }


	 @PostMapping("/FindFriends")
	 public String findFriends(
			 @RequestParam String mainString,
			 @RequestParam String hobby,
			 @RequestParam String workPlace,
			 @RequestParam String studyPlace,
			 Model model, RedirectAttributes redirectAttributes
			 )
	 {
		 if(!mainString.isBlank())
			 redirectAttributes.addFlashAttribute("users", service.searchFriends(mainString, workPlace, studyPlace, hobby));
		 
		 return "redirect:/Friends?type=search";
		 
	 }
	 
	 @GetMapping("/send-request")
	 public String sendRequest(@RequestParam String userId)
	 {
		 service.sendRequest(Long.parseLong(userId));
		 return "redirect:/user?userId=" + userId;
	 }

	 @GetMapping("/block-user")
	 public String setBloking(@RequestParam String userId, @RequestParam String action) {
		 
		 service.setBlocking(action, Long.parseLong(userId));
		 return "redirect:/user?userId=" + userId;
		 
	 }
	 
	 @GetMapping("/deletePost")
	 public String deletePost(@RequestParam String id) {
		 
		 
		 newsServ.deletePost(Long.parseLong(id));
		 return "redirect:/News?type=worst";
	 }
}
