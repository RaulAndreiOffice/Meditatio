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

@Service
@Getter
@Setter
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Aici poți adăuga și roluri default la înregistrare
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
}
