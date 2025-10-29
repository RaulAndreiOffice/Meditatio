package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import server.model.Assignments;
import server.repository.AssignmentRepository;
import server.repository.AssignmentSubmissionRepository;
import server.repository.UserRepository;
import server.service.AzureStorageService; // Importă noul serviciu
import org.springframework.http.HttpStatus;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication; // Pentru a obține user-ul logat
import server.model.Assignments;
import server.model.User;
import server.repository.AssignmentRepository;
import server.repository.AssignmentSubmissionRepository;
import server.repository.UserRepository;
import server.service.AzureStorageService;
import server.service.UserService; // Avem nevoie de UserService
import java.util.List;
import server.service.AssignmentService;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {



    @Autowired
    private AssignmentSubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    // --- NOU: Injectăm serviciul Azure ---
    @Autowired
    private AzureStorageService azureStorageService;

    @Autowired
    private UserService userService;
    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('PROFESOR')") // Securizăm endpoint-ul
    public ResponseEntity<?> createAssignment(
            // Folosim @RequestParam pentru a citi datele din FormData
            // Numele ("asstitle", "description"...) trebuie să fie identice
            // cu cele folosite în React (CreateAssignment.tsx)
            @RequestParam("asstitle") String title,
            @RequestParam("description") String description,
            @RequestParam("studentId") Long studentId,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            // Apelăm metoda de serviciu care face toată treaba
            Assignments createdAssignment = azureStorageService.createAssignment(title, description, studentId, file);
            // Returnăm tema creată
            return ResponseEntity.ok(createdAssignment);

        } catch (Exception e) {
            // Prindem erorile (ex: Student not found)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la crearea temei: " + e.getMessage());
        }
    }
    @GetMapping("/student/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Assignments>> getAssignmentsForCurrentUser(Authentication authentication) {
        // 1. Obține numele studentului logat
        String username = authentication.getName();
        // 2. Găsește obiectul User
        User student = userService.findByUsername(username);
        // 3. Caută temele folosind metoda din repository
        List<Assignments> assignments = assignmentRepository.findAssignmentsByStudentId(student.getUser_id());
        // 4. Returnează lista
        return ResponseEntity.ok(assignments);
    }

    // --- ENDPOINT NOU (2): Pentru a lua detaliile unei singure teme ---
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Assignments> getAssignmentById(@PathVariable Integer id) {
        // Caută tema după ID și o returnează; altfel, returnează 404 Not Found
        return assignmentRepository.findById(id)
                .map(ResponseEntity::ok) // Echivalent cu .map(assignment -> ResponseEntity.ok(assignment))
                .orElse(ResponseEntity.notFound().build());
    }

    // --- NOU: ENDPOINT PENTRU PROFESOR ---
    @GetMapping("/professor/me")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<List<Assignments>> getAssignmentsForCurrentProfessor(Authentication authentication) {
        // 1. Găsește profesorul logat
        User professor = userService.findByUsername(authentication.getName());
        // 2. Caută temele folosind noua metodă din repository
        List<Assignments> assignments = assignmentRepository.findAssignmentsByProfessorUserId(professor.getUser_id());
        // 3. Returnează lista
        return ResponseEntity.ok(assignments);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<?> deleteAssignment(@PathVariable Integer id, Authentication authentication) {
        try {
            User professor = userService.findByUsername(authentication.getName());
            boolean deleted =assignmentService.deleteAssignment(id, professor);
            if (deleted) {
                return ResponseEntity.ok().build(); // Succes, 200 OK
            } else {
                return ResponseEntity.notFound().build(); // Nu a fost găsită, 404
            }
        } catch (SecurityException e) {
            // Prins dacă profesorul încearcă să șteargă tema altcuiva
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

}