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
import server.model.AssignmentSubmission;
import java.util.Optional;
import server.DTO.SubmissionDetailsDTO;
import java.util.stream.Collectors;
import server.DTO.GradeRequest;
import server.DTO.GradedSubmissionViewDTO;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {



    @Autowired
    private AssignmentSubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;


    @Autowired
    private AzureStorageService azureStorageService;

    @Autowired
    private UserService userService;
    @Autowired
    private AssignmentService assignmentService;


    //  ENDPOINT PENTRU STUDENT

    @GetMapping("/submission/graded-view/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getGradedSubmissionView(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        try {
            User student = userService.findByUsername(authentication.getName());
            AssignmentSubmission submission = assignmentService.getGradedSubmissionForStudent(id, student);

            // Creăm un DTO curat pentru a-l trimite studentului
            GradedSubmissionViewDTO dto = new GradedSubmissionViewDTO();
            dto.setAssignmentTitle(submission.getAssignment().getAsstitle());
            dto.setGrade(submission.getGrade());
            dto.setStudentSubmissionUrl(submission.getGoogleDriveFileId());
            dto.setProfessorDrawingData(submission.getProfessorFeedbackDrawing());

            return ResponseEntity.ok(dto);

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    //  ENDPOINT PENTRU A ACORDA NOTĂ

    @PostMapping("/submission/{id}/grade")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<?> gradeAssignment(
            @PathVariable Integer id,
            @RequestBody GradeRequest gradeRequest,
            Authentication authentication
    ) {
        try {
            User professor = userService.findByUsername(authentication.getName());
            AssignmentSubmission gradedSubmission = assignmentService.gradeSubmission(id, gradeRequest, professor);
            return ResponseEntity.ok(gradedSubmission);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ENDPOINT PENTRU STUDENT (SĂ VADĂ NOTELE)
    @GetMapping("/submissions/student/graded")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<SubmissionDetailsDTO>> getGradedSubmissionsForStudent(Authentication authentication) {
        // 1. Găsește studentul logat
        User student = userService.findByUsername(authentication.getName());

        // 2. Caută rezolvările notate
        List<AssignmentSubmission> submissions = submissionRepository.findGradedSubmissionsForStudent(student.getUser_id());

        // 3. Transformă în DTO-uri
        List<SubmissionDetailsDTO> dtos = submissions.stream().map(sub -> {
            SubmissionDetailsDTO dto = new SubmissionDetailsDTO();
            dto.setSubmissionId(sub.getSubmissionId());
            dto.setSubmittedAt(sub.getSubmittedAt()); // Aceasta e data trimiterii
            dto.setGrade(sub.getGrade());

            // Student
            SubmissionDetailsDTO.StudentDTO studentDTO = new SubmissionDetailsDTO.StudentDTO();
            studentDTO.setUsername(sub.getStudent().getUsername());
            dto.setStudent(studentDTO);

            // Assignment
            SubmissionDetailsDTO.AssignmentDTO assignmentDTO = new SubmissionDetailsDTO.AssignmentDTO();
            assignmentDTO.setAssid(sub.getAssignment().getAssid());
            assignmentDTO.setAsstitle(sub.getAssignment().getAsstitle());
            dto.setAssignment(assignmentDTO);

            return dto;
        }).collect(Collectors.toList());

        // 4. Returnează lista
        return ResponseEntity.ok(dtos);
    }

    // ENDPOINT PENTRU PROFESOR
    @GetMapping("/submissions/professor/me")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<List<SubmissionDetailsDTO>> getAllSubmissionsForProfessor(Authentication authentication) {
        // 1. Găsește profesorul logat
        User professor = userService.findByUsername(authentication.getName());

        // 2. Caută rezolvările (Entitățile)
        List<AssignmentSubmission> submissions = submissionRepository.findAllSubmissionsForProfessor(professor.getUser_id());

        // 3. Transformă Entitățile în DTO-uri
        List<SubmissionDetailsDTO> dtos = submissions.stream().map(sub -> {
            SubmissionDetailsDTO dto = new SubmissionDetailsDTO();
            dto.setSubmissionId(sub.getSubmissionId());
            dto.setSubmittedAt(sub.getSubmittedAt());

            // Creăm DTO-ul pentru Student
            SubmissionDetailsDTO.StudentDTO studentDTO = new SubmissionDetailsDTO.StudentDTO();
            studentDTO.setUsername(sub.getStudent().getUsername());
            dto.setStudent(studentDTO);

            // Creăm DTO-ul pentru Assignment
            SubmissionDetailsDTO.AssignmentDTO assignmentDTO = new SubmissionDetailsDTO.AssignmentDTO();
            assignmentDTO.setAssid(sub.getAssignment().getAssid());
            assignmentDTO.setAsstitle(sub.getAssignment().getAsstitle());
            dto.setAssignment(assignmentDTO);

            return dto;
        }).collect(Collectors.toList());

        // 4. Returnează lista de DTO-uri
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitAssignment(
            @RequestParam("assignmentId") Integer assignmentId,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {
        try {
            // Găsim studentul logat
            User student = userService.findByUsername(authentication.getName());

            // Apelăm serviciul
            AssignmentSubmission submission = assignmentService.submitAssignment(assignmentId, description, file, student);

            return ResponseEntity.ok(submission);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la trimiterea temei: " + e.getMessage());
        }
    }

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
            Assignments createdAssignment = assignmentService.createAssignment(title, description, studentId, file);
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

    // Pentru a lua detaliile unei singure teme
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Assignments> getAssignmentById(@PathVariable Integer id) {
        // Caută tema după ID și o returnează; altfel, returnează 404 Not Found
        return assignmentRepository.findById(id)
                .map(ResponseEntity::ok) // Echivalent cu .map(assignment -> ResponseEntity.ok(assignment))
                .orElse(ResponseEntity.notFound().build());
    }

    // ENDPOINT PENTRU PROFESOR
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
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SecurityException e) {
            // Prins dacă profesorul încearcă să șteargă tema altcuiva
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // ENDPOINT PENTRU A OBȚINE REZOLVAREA STUDENTULUI
    @GetMapping("/submission/{assignmentId}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<AssignmentSubmission> getSubmissionForAssignment(
            @PathVariable Integer assignmentId
    ) {
        // Căutăm rezolvarea folosind noua metodă din repository
        Optional<AssignmentSubmission> submission = submissionRepository.findFirstByAssignmentAssidOrderBySubmittedAtDesc(assignmentId);



        return submission
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}