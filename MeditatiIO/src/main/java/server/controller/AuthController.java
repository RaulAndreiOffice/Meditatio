package server.controller;
import server.model.User;
import server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    // Alte injectări necesare pentru login (AuthenticationManager, JwtTokenProvider)

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Aici poți adăuga validări
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(/* @RequestBody LoginRequest loginRequest */) {
        // Logica de login cu Spring Security
        // Va genera un token JWT la succes
        return ResponseEntity.ok("User registered successfully!");
    }
}
