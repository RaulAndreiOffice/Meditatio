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
import server.model.AssignmentSubmission; // <-- Adaugă acest import
import server.repository.AssignmentSubmissionRepository; // <-- Adaugă acest import
import org.springframework.security.core.Authentication; // <-- Adaugă acest import
import org.springframework.security.core.context.SecurityContextHolder; // <-- Adaugă acest import
import server.DTO.GradeRequest; // <-- NOU
import org.springframework.transaction.annotation.Transactional; // <-- NO

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

    @Autowired
    private AssignmentSubmissionRepository submissionRepository;

    @Transactional(readOnly = true)
    public AssignmentSubmission getGradedSubmissionForStudent(Integer submissionId, User student) {
        // 1. Găsim rezolvarea
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Rezolvarea cu ID " + submissionId + " nu a fost găsită."));

        // 2. Verificare de securitate: Doar studentul care a trimis-o o poate vedea
        if (!submission.getStudent().getUser_id().equals(student.getUser_id())) {
            throw new SecurityException("Nu aveți permisiunea de a vizualiza această corectură.");
        }

        // 3. Verificăm dacă a fost notată
        if (submission.getGrade() == null || submission.getProfessorFeedbackDrawing() == null) {
            throw new RuntimeException("Această temă nu a fost încă corectată.");
        }

        return submission;
    }

    @Transactional
    public AssignmentSubmission gradeSubmission(Integer submissionId, GradeRequest gradeRequest, User currentProfessor) {
        // 1. Găsim rezolvarea (submission)
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Rezolvarea cu ID " + submissionId + " nu a fost găsită."));

        // 2. Verificare de securitate: Doar profesorul care a creat tema poate nota
        Integer professorId = submission.getAssignment().getProfessor().getUser_id();
        if (!professorId.equals(currentProfessor.getUser_id())) {
            throw new SecurityException("Nu aveți permisiunea de a nota această temă.");
        }

        // 3. Setăm nota
        submission.setGrade(gradeRequest.getGrade());

        submission.setProfessorFeedbackDrawing(gradeRequest.getFeedbackDrawing());

        // 4. Salvăm și returnăm
        return submissionRepository.save(submission);
    }

    public AssignmentSubmission submitAssignment(Integer assignmentId, String description, MultipartFile file, User student) throws Exception {

        // 1. Găsim tema originală
        Assignments assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new Exception("Tema cu ID " + assignmentId + " nu a fost găsită."));

        // 2. Verificăm dacă studentul are voie să trimită la această temă
        if (!assignment.getStudent().getUser_id().equals(student.getUser_id())) {
            throw new SecurityException("Nu aveți permisiunea de a trimite la această temă.");
        }

        // 3. Încărcăm fișierul în Azure
        String fileUrl = azureStorageService.upload(file);

        // 4. Creăm și salvăm înregistrarea rezolvării
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setStudent(student);
        submission.setAssignment(assignment);
        submission.setDescription(description);
        submission.setGoogleDriveFileId(fileUrl); // ATENȚIE: Câmpul tău se numește 'googleDriveFileId'
        submission.setOriginalFileName(file.getOriginalFilename());

        return submissionRepository.save(submission);
    }


    //  CREATE ASSIGNMENT (Moved from AzureStorageService)
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

    // UPDATE ASSIGNMENT
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

    //  DELETE ASSIGNMENT
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
