package Classes.BuisnessLogic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import Classes.Models.*;
import Classes.Repository.*;

@Service
public class NewsService {

	
	@Autowired 
	private NewsRepository newsRepo;
	
	@Autowired
	private UsersRepository usersRepo;
	
	
	@Autowired
	private ContactsRepository contactsRepo;
	
	@Autowired
	private CommentRepositoty commRepo;
	
	@Autowired
	private LikesRepository likeRepo;
	
	public void getAllNews(Model model, String sortType)
	{
		var user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<News> list = new ArrayList<News>();
		
		var contacts = contactsRepo.getFriends(user.getId());
		
		list.addAll(newsRepo.findAllByAuthor(user));
		for(var c: contacts) {
			c.reverceContact(user.getId());
			list.addAll(newsRepo.findAllByAuthor(c.getContact()));
			
		}
		var likedNews = likeRepo.getLikedNews(user.getId());
		
		if(likedNews != null) {
			System.out.print("check");
			for(var elem: list) {
				if(likedNews.contains(elem.getId()))
					elem.setIsLiked(true);
				else
					elem.setIsLiked(false);
			}
		}
		
		
		
		list.sort(new NewsSorter().getComparator(sortType));
		model.addAttribute("News", list);
		model.addAttribute("User", user.getId());
		model.addAttribute("admin", user.getIsAdmin());
		
	}
	
	
	public void SetComment(WebSocketComment comm) {
		
		var comment = new Comment();
		comment.setText(comm.getText());
		comment.setDate(LocalDateTime.now());
		comment.setAuthor(usersRepo.findById(comm.getAuthor()).get());
		comment.setCommentNew(newsRepo.findById(comm.getMyNew()).get());
		
		commRepo.save(comment);
	}
	
	public void CreateNew(String text) {
		var post = new News();
		var user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		post.setText(text);
		post.setDate(LocalDateTime.now());
		post.setAuthor(user);
		post.setRating(0);
		
		newsRepo.save(post);
	}
	
	public void setLike(Likes like) {
	
		
		var post = newsRepo.findById(like.getNewsId()).get();
		post.setRating(post.getRating() + like.getValue());
		newsRepo.save(post);
		
		likeRepo.save(like);
	}
	
	public void deletePost(Long id) {
		newsRepo.deleteById(id);
	}
}
