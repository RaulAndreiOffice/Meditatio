package server.controller;
import server.model.User;
import server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import server.model.Role;
import java.util.List;
import java.util.stream.Collectors;
import server.repository.RoleRepository;
import server.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;


    //  Endpoint pentru UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails)
                .map(user -> ResponseEntity.ok("Utilizator actualizat cu succes!"))
                .orElse(ResponseEntity.notFound().build());
    }

    //DELETE USER
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok("Cont șters cu succes!");
        }
        return ResponseEntity.notFound().build();
    }

    // GET TOȚI STUDENȚII
    @GetMapping("/students")
    @PreAuthorize("hasRole('ROLE_PROFESOR')")
    public ResponseEntity<List<User>> getAllStudents() {
        try {
            Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                    .orElseThrow(() -> new RuntimeException("Rolul ROLE_STUDENT nu a fost găsit."));

            List<User> students = userRepository.findByRolesContaining(studentRole);

            System.out.println("== STUDENTS FOUND ==");
            students.forEach(u -> System.out.println(u.getUsername() + " -> " + u.getRoles()));

            return ResponseEntity.ok(students);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
