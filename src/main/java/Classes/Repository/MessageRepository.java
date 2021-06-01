package Classes.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import Classes.Models.*;

public interface MessageRepository  extends CrudRepository<Messages, Long>{

	@Query(value = "SELECT * FROM messages WHERE (author_id = ?1 AND recip_id = ?2) OR (author_id = ?2 AND recip_id = ?1) ORDER BY date_time", nativeQuery = true)
	List<Messages> findDialog(Long author, Long recipient);
}
