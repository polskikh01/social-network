package Classes.Models;

import java.beans.Transient;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Messages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
	@Column(name = "message")
    private String message;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "recip_id") 
	private Users recipient;
	 
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "author_id") 
	private Users author;
	
	@Column(name = "dateTime")
	private LocalDateTime dateTime;
	

	private String imageUrl;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public Users getRecipient() {
		return recipient;
	}

	public void setRecipient(Users recipient) {
		this.recipient = recipient;
	}


	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	
	@Transient
	public boolean getImageExist() {
		return imageUrl != "";
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Transient
	public String getDeltatime() {
		
		
		var currTime = LocalDateTime.now();
		
		long minutes = Duration.between(dateTime, currTime).toMinutes();
		

		
			
		if(minutes < 1)
			return "Меньше минуты назад";
		
		if(minutes < 60) {
			int lastNumber = (int) (minutes % 10);
			if(lastNumber == 1)
				return minutes + " минуту назад";
			if(lastNumber >= 2 && lastNumber < 5)
				return minutes + " минуты назад";
			
			return minutes + " минут назад";
	
		}
		else if(minutes < 1440)
		{
			return minutes/60 + " часа назад";
		}
		else
			return minutes/1440 + " дней назад";
		
		
	}
	

}
