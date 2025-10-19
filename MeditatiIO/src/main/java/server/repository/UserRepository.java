package server.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import server.model.Role;
import server.model.User;
import java.util.Optional;



public interface UserRepository extends JpaRepository<User, Integer> {
     Optional<Role> findByUsername(String username);
}
