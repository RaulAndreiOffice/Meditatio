package server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_submissions")
@Getter
@Setter
@NoArgsConstructor
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "submissionId") // <-- ȘTERGE ASTA
    private Integer submissionId; // Java: submissionId -> SQL: submission_id (Acum e corect)

    // @Column(name = "googleDriveFileId", nullable = false) // <-- ȘTERGE ASTA
    @Column(nullable = false) // Păstrează doar 'nullable' dacă vrei
    private String googleDriveFileId; // Java: googleDriveFileId -> SQL: google_drive_file_id

    // @Column(name = "originalFileName") // <-- ȘTERGE ASTA
    private String originalFileName; // Java: originalFileName -> SQL: original_file_name

    // @Column(name = "submittedAt", updatable = false) // <-- ȘTERGE ASTA
    @Column(updatable = false) // Păstrează doar 'updatable'
    @CreationTimestamp
    private LocalDateTime submittedAt; // Java: submittedAt -> SQL: submitted_at

    // Acestea sunt corecte
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignments assignment;

    // Constructorul
    public AssignmentSubmission(String googleDriveFileId, String originalFileName, User student, Assignments assignment) {
        this.googleDriveFileId = googleDriveFileId;
        this.originalFileName = originalFileName;
        this.student = student;
        this.assignment = assignment;
    }
}