package server.DTO;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class SubmissionDetailsDTO {
    private Integer submissionId;
    private LocalDateTime submittedAt;
    private StudentDTO student;
    private AssignmentDTO assignment;
    private Integer grade;

    // Clas interioar pentru a ține doar numele studentului
    @Getter
    @Setter
    public static class StudentDTO {
        private String username;
    }


    @Getter
    @Setter
    public static class AssignmentDTO {
        private Integer assid;
        private String asstitle;
    }
}
