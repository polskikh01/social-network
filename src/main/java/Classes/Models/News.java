package Classes.Models;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
@Entity
public class News  {

	
	
	

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "text")
	private String text;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id")
	private Users author;
	


	@Column(name = "date")
	private LocalDateTime date;

	@Column(name = "rating")
	private Integer rating;
	
	@Transient
	private Boolean isLiked;
	
	private String image_url;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "commentNew") 
	private Set<Comment> comments;
	
	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}
	
	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		if(image_url.length() == 0)
			this.image_url = null;
		else
			this.image_url = image_url;
	}

	

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@Transient
	public String getDeltaTime(){
		
		
		var currTime = LocalDateTime.now();
		
		long minutes = Duration.between(date, currTime).toMinutes();
		

		
			
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

	public Boolean getIsLiked() {
		return isLiked;
	}

	public void setIsLiked(Boolean isLiked) {
		this.isLiked = isLiked;
	}

	public News() {
		isLiked = false;
	}
	
	
	
	
	
	
}
