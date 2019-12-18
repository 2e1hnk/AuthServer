package cafe.deadbeef.auth_server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, String> {

	@Transactional
	@Modifying
	@Query(value = "INSERT IGNORE INTO authorities (username, authority) VALUES (:username, :role)", nativeQuery = true)
	void grantRole(@Param("username") String username, @Param("role") String role);
}
