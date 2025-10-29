package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import server.DTO.AssignmentUpdateRequest;
import server.model.Assignments;
import server.model.User;
import server.repository.AssignmentRepository;
import server.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AzureStorageService azureStorageService; // Inject Azure service for uploads

    // --- CREATE ASSIGNMENT (Moved from AzureStorageService) ---
    public Assignments createAssignment(String title, String description, Long studentId, MultipartFile file) throws Exception {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User professor = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Profesorul logat nu a fost găsit: " + currentUsername));

        User student = userRepository.findById(studentId.intValue())
                .orElseThrow(() -> new Exception("Studentul cu ID " + studentId + " nu a fost găsit."));

        String fileUrl = null;
        if (file != null && !file.isEmpty()) {
            // Use the injected Azure service
            fileUrl = azureStorageService.upload(file);
        }

        Assignments assignment = new Assignments();
        assignment.setAsstitle(title);
        assignment.setDescription(description);
        assignment.setStudent(student);
        assignment.setProfessor(professor);
        assignment.setFileUrl(fileUrl);

        return assignmentRepository.save(assignment);
    }

    // --- NEW: UPDATE ASSIGNMENT ---
    public Optional<Assignments> updateAssignment(Integer assignmentId, AssignmentUpdateRequest request, User currentProfessor) {
        // Find the existing assignment
        return assignmentRepository.findById(assignmentId)
                .map(assignment -> {
                    // Security check: Only the professor who created it can modify it
                    if (!assignment.getProfessor().getUser_id().equals(currentProfessor.getUser_id())) {
                        throw new SecurityException("Nu aveți permisiunea de a modifica această temă.");
                    }

                    // Update fields if they are provided in the request
                    if (request.getTitle() != null && !request.getTitle().isEmpty()) {
                        assignment.setAsstitle(request.getTitle());
                    }
                    if (request.getDescription() != null && !request.getDescription().isEmpty()) {
                        assignment.setDescription(request.getDescription());
                    }
                    // Update student if a new ID is provided
                    if (request.getStudentId() != null) {
                        User newStudent = userRepository.findById(request.getStudentId().intValue())
                                .orElseThrow(() -> new RuntimeException("Studentul nou cu ID " + request.getStudentId() + " nu a fost găsit."));
                        assignment.setStudent(newStudent);
                    }
                    // Save and return the updated assignment
                    return assignmentRepository.save(assignment);
                });
    }

    // --- NEW: DELETE ASSIGNMENT ---
    public boolean deleteAssignment(Integer assignmentId, User currentProfessor) {
        Optional<Assignments> assignmentOpt = assignmentRepository.findById(assignmentId);
        if (assignmentOpt.isPresent()) {
            Assignments assignment = assignmentOpt.get();
            // Security check
            if (!assignment.getProfessor().getUser_id().equals(currentProfessor.getUser_id())) {
                throw new SecurityException("Nu aveți permisiunea de a șterge această temă.");
            }
            assignmentRepository.deleteById(assignmentId);
            return true;
        }
        return false; // Assignment not found
    }

}
