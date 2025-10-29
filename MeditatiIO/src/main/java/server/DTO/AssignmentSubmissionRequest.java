package server.DTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AssignmentSubmissionRequest {
    private Long assignmentId;
    private Long studentId;
    private MultipartFile file;
}
