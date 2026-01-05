package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import server.model.User;
import server.service.UserService;
import server.repository.UserRepository;
import java.util.List;

import server.model.Assignments;
import server.model.Role;
import server.repository.AssignmentRepository;
import server.repository.RoleRepository;
import server.service.UserService;
import server.DTO.RegisterRequest;



@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')") // Securizăm tot controllerul
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    // Endpoint pentru a vedea TOȚI utilizatorii
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    //ENDPOINTS PENTRU USERI

    @GetMapping("/users/students")
    public List<User> getStudents() {
        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Rolul ROLE_STUDENT nu a fost găsit."));
        return userRepository.findByRolesContaining(studentRole);
    }

    @GetMapping("/users/professors")
    public List<User> getProfessors() {
        Role profRole = roleRepository.findByName("ROLE_PROFESOR")
                .orElseThrow(() -> new RuntimeException("Rolul ROLE_PROFESOR nu a fost găsit."));
        return userRepository.findByRolesContaining(profRole);
    }

    @PostMapping("/users/create")
    public User createUser(@RequestBody RegisterRequest registerRequest) {
        // Reutilizăm logica de înregistrare
        return userService.registerUser(registerRequest);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Utilizator șters.");
    }

    //  ENDPOINTS PENTRU TEME

    @GetMapping("/assignments")
    public List<Assignments> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Integer id) {
        //  Această logică șterge tema indiferent de cine a creat-o

        assignmentRepository.deleteById(id);
        return ResponseEntity.ok("Temă ștearsă.");
    }

}
