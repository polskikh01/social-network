package Classes.Controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Classes.BuisnessLogic.EmailSenderService;
import Classes.BuisnessLogic.UserService;
import Classes.Models.AccountState;
import Classes.Models.Users;

@Controller
public class RegistrationController {

	@Autowired
	UserService service;

	@Autowired
	EmailSenderService mailSender;
	
	
	
	@PostMapping("/Registration")
	public String registration(
			@RequestParam String name, 
			@RequestParam String se_name, 
			@RequestParam String username, 
			@RequestParam String password, 
			@RequestParam String confirm_password,
			Model model)
	{
		
		
		Users user = new Users();
		user.setUsername(username);
		user.setPassword(password);
		user.setSeName(name);
		user.setName(se_name);
		
		
		if(service.CreateAccount(user, confirm_password, model))
			System.out.print("created");
		else
			System.out.print("error");
		return "redirect:/Login";
	}
	
	
	@GetMapping("/confirm-account")
	public String confirm(@RequestParam("value") String value, @RequestParam String user) {
		
		var account = (Users)service.loadUserByUsername(user);
		
		
		if(account.getConfirmValue().equals(value))
		{
			account.setState(AccountState.Active);
			
			service.SaveUser(account);
			
			return "redirect:/";
		}
		else {
			return null;
		}
		
	}
}
