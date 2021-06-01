package Classes.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import Classes.Models.*;


@Repository
public interface NewsRepository extends CrudRepository<News, Long>{
	
	List<News> findAllByAuthor(Users author);
	
}
