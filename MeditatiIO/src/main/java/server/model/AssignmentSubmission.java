package server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "assignment_submissions")
@Getter
@Setter
@NoArgsConstructor
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer submissionId;


    @Column(nullable = false)
    private String googleDriveFileId;


    private String originalFileName;


    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime submittedAt;



    @Column(name = "grade")
    private Integer grade;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    @JsonBackReference
    private Assignments assignment;

    @Column(name = "professor_feedback_drawing", columnDefinition = "TEXT")
    private String professorFeedbackDrawing;

    @Column(columnDefinition = "TEXT")
    private String description;


    public AssignmentSubmission(String googleDriveFileId, String originalFileName, User student, Assignments assignment, String description,String professorFeedbackDrawing) {
        this.googleDriveFileId = googleDriveFileId;
        this.originalFileName = originalFileName;
        this.student = student;
        this.assignment = assignment;
        this.description = description;
        this.professorFeedbackDrawing = professorFeedbackDrawing;
    }
}