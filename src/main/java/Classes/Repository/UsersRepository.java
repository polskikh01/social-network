package Classes.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import Classes.Models.Users;


@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {

	Users findByUsername(String name);
	

	@Query(value = "SELECT * FROM users WHERE ((name LIKE ?1% AND se_name LIKE ?2%) OR (name LIKE ?2% AND se_name LIKE ?1%))", nativeQuery = true)
	List<Users> findByNames(String name, String seName);
	
	@Query(value = "SELECT * FROM users WHERE name LIKE ?1% OR se_name LIKE ?1%", nativeQuery = true)
	List<Users> findAllByName(String name);
}
