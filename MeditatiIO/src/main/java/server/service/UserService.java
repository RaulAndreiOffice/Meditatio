package server.service;

import lombok.Getter;
import lombok.Setter;
import server.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import server.repository.RoleRepository;
import server.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import server.DTO.RegisterRequest;
import java.util.Set;
import java.util.HashSet;

@Service
@Getter
@Setter
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public User registerUser(RegisterRequest registerRequest) {
       User user = new User();
       user.setUsername(registerRequest.getUsername());
       user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

       //Logica pentru atribuirea roluriilor

        Role userRole = roleRepository.findByName(registerRequest.getRoleName()).
                orElseThrow(() -> new RuntimeException("Role not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        user.setUsergrade(0.0);

        return userRepository.save(user);

    }
    // NOU: Metoda pentru actualizarea datelor
    public Optional<User> updateUser(Integer userId, User userDetails) {
        return userRepository.findById(userId).map(user -> {
            // Actualizăm doar câmpurile care sunt furnizate
            if (userDetails.getUsername() != null && !userDetails.getUsername().isEmpty()) {
                user.setUsername(userDetails.getUsername());
            }
            // Verificăm dacă parola nouă este furnizată înainte de a o encoda
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
            return userRepository.save(user);
        });
    }
    // NOU: Metoda pentru ștergerea contului
    public boolean deleteUser(Integer userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}
