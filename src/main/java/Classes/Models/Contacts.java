package Classes.Models;

import java.beans.Transient;
import java.sql.Date;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Contacts {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	
	@Column(name = "Status")
	private Status status;
	
	private String lastMessage;
	
	private LocalDateTime lastTime;
	
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "contact_id")
	private Users contact;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id")
	private Users user;
	
	public Users getContact() { return contact; }
	  
	public void setContact(Users contact) { this.contact = contact; }
	 

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}


	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public LocalDateTime getLastTime() {
		return lastTime;
	}

	public void setLastTime(LocalDateTime lastTime) {
		this.lastTime = lastTime;
	}

	public Contacts(Status status, Users contact, Users user) {
		
		this.status = status;
		this.contact = contact;
		this.user = user;
	}

	public Contacts() {
		
	}


	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	
   	public void reverceContact(Long id) {
   		if(id == contact.getId()) {
   			Users u = user;
   	   		user = contact;
   	   		contact = u;
   		}

   	}
   	
   	@Transient
   	public String getFriendStatus(){
   		switch(status) {
   		
   			case Request:
   				return "Запрос в друзья отправлен";
   			case Friends:
   				return "Этот пользователь у вас в друзьях";
   			default:
   				return "button";
   		}
   	}
}
