package Classes.Repository;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import Classes.Models.*;


@Repository
public interface ContactsRepository extends CrudRepository<Contacts, Long> {

	
	@Query(value = "SELECT * FROM contacts  WHERE (user_id = ?1 OR contact_id = ?1) AND (last_message IS NOT NULL) ORDER BY last_time", nativeQuery = true)
	List<Contacts> findAllContacts(Long id);
	

	@Query(value = "SELECT * FROM contacts WHERE (user_id = ?1 OR contact_id = ?1) AND status = 'F'", nativeQuery = true)
	List<Contacts> getFriends(Long id);
	
	@Query(value = "SELECT * FROM contacts WHERE (user_id = ?1 OR contact_id = ?1) AND status = 'R'", nativeQuery = true)
	List<Contacts> getRequests(Long id);
	
	
	@Query(value = "SELECT * FROM contacts WHERE (user_id = ?1 AND contact_id = ?2) OR (user_id = ?2 AND contact_id = ?1)", nativeQuery = true)
	Contacts getContactsById(Long userId, Long contactId);
	

	@Query(value = "SELECT * FROM contacts WHERE contact_id = ?1 AND status = 'R'", nativeQuery = true)
	List<Contacts> getAllRequests(Long id);
}