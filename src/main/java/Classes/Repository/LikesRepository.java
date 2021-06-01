package Classes.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import Classes.Models.*;


@Repository
public interface LikesRepository extends CrudRepository<Likes, Long> {

	
	@Query(value = "SELECT news_id FROM likes WHERE author_id = ?1", nativeQuery = true)
	List<Long> getLikedNews(Long id);
}
