package Classes.Models;
import java.beans.Transient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class UserInfo {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	private String About;
	
	private Date bornDate;
	
	private String studyPlace;
	
	private String workPlace;
	
	private String hobby;

	private String avatar;
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getAbout() {
		return About;
	}

	public void setAbout(String about) {
		if(about.length() == 0)
			About = null;
		else
			About = about;
	}

	public Date getBornDate() {
		return bornDate;
	}

	public void setBornDate(Date bornDate) {

		this.bornDate = bornDate;
	}

	public String getStudyPlace() {
		return studyPlace;
	}

	public void setStudyPlace(String studyPlace) {
		if(studyPlace.length() == 0)
			this.studyPlace = null;
		else
			this.studyPlace = studyPlace;
	}

	public String getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(String workPlace) {
		if(workPlace.length() == 0)
			this.workPlace = null;
		else
			this.workPlace = workPlace;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		if(hobby.length() == 0)
			this.hobby = null;
		else
			this.hobby = hobby;
		
		
	}
	
	@Transient
	public String getDate() {
		
		if(bornDate == null)
			return null;
		
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
		return format.format(bornDate);
	}

	public UserInfo( String about, String bornDate, String studyPlace, String workPlace, String hobby) {
		About = about;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.bornDate = format.parse(bornDate);
		} catch (ParseException e) {
			this.bornDate = null;
		}
		
		
		this.studyPlace = studyPlace;
		this.workPlace = workPlace;
		this.hobby = hobby;
	}
	
	public UserInfo() {
		
	}
	
	public void setInfo(UserInfo info) {
		setAbout(info.About);
		setBornDate(info.bornDate);
		setWorkPlace(info.workPlace);
		setStudyPlace(info.studyPlace);
		setHobby(info.hobby);
		if(info.avatar != null)
			avatar = info.avatar;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		if(avatar == null || avatar.length() == 0) {
			avatar = "/img/default_avatar.jpg";
		}
		this.avatar = "/upload/" + avatar;
	}
	
	

}
