package Classes.Repository;

import org.springframework.data.repository.CrudRepository;

import Classes.Models.Comment;


public interface CommentRepositoty  extends CrudRepository<Comment, Long> {

}
