package server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "assignments")
@Getter
@Setter
public class Assignments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assid;

    private String asstitle;
    private String description;

    @Column(name = "createdat", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid", referencedColumnName = "user_id")
    private User student;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id", referencedColumnName = "user_id")
    private User professor;

    // --- MODIFICARE CRITICĂ ---
    // Am redenumit 'googleDrivePromptImageId' în 'fileUrl'
    // pentru a stoca link-ul de la Azure.
    @Column(name = "file_url")
    private String fileUrl;
}