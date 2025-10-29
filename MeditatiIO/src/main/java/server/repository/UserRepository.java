package server.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import server.model.Role;
import server.model.User;
import java.util.Optional;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
     Optional<User> findByUsername(String username);

     List<User> findByRolesContaining(Role role);

}
